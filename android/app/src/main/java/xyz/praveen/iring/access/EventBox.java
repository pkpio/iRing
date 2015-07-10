package xyz.praveen.iring.access;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import xyz.praveen.iring.model.Event;
import xyz.praveen.iring.util.OnHitrateChangeListener;

import static xyz.praveen.iring.util.LogUtils.LOGD;
import static xyz.praveen.iring.util.LogUtils.LOGE;
import static xyz.praveen.iring.util.LogUtils.LOGI;
import static xyz.praveen.iring.util.LogUtils.makeLogTag;

/**
 * Takes events from Gadgeteer, Touch screen and performs matching.
 * Also issues lock signals based on current hitrate and history.
 * <p/>
 * Created by praveen on 9/7/15.
 */
public class EventBox {
    final static String TAG = makeLogTag(EventBox.class);

    /**
     * Max. delay (in milliseconds ) before which a gadget event
     * is received by the phone after the corresponding touch event occurs
     */
    static final long GADGET_EVENT_DELAY = 1000;

    /**
     * Max. delay (in milliseconds ) before which a touch event
     * is received by the phone after the corresponding gadget event occurs
     */
    static final long TOUCH_EVENT_DELAY = 1000;

    /**
     * Size of action history over which error rate is calculated
     */
    static final int HISTORY_SIZE = 10;

    /**
     * Hit or fail history over last 10 events
     */
    static List<Integer> mHistory = new ArrayList<>(10);

    /**
     * Maximum hit rate achieved through out the entire history
     */
    static float mMaxHitRate = 0;

    /**
     * Min value of the Max hit rate after which locking happens if hit rate fell
     * below the HitRateThreshold. This is to ensure that locking won't happen during
     * initial device setup.
     * <p/>
     * So, to sum up, no locking for low hitrates until hit rate reaches this
     * value at least once in it's history after application started.
     */
    static final int MIN_MAX_HITRATE_LOCK = 40;

    /**
     * Hit rate below which locking happens. This works only after max hit rate
     * reaches MIN_MAX_HITRATE_LOCK
     */
    static final int MIN_HITRATE_LOCK = 40;

    static Handler mHandlerTouch;
    static Handler mHandlerGadget;

    static Event mTouchEvent;
    static Event mGadgetEvent;
    public static Context mContext;
    public static OnHitrateChangeListener hitrateChangeListener;

    /**
     * Send a touch event to the event box
     *
     * @param action Action code.
     */
    public synchronized static void sendTouchEvent(int action) {
        mTouchEvent = new Event(action, System.currentTimeMillis());

        if (mGadgetEvent == null) {

            // and see if the gadget event is set in next 50 ms
            mHandlerTouch = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    if (mGadgetEvent == null) {
                        LOGE(TAG, "No corresponding gadget event");
                        recordHistory(false);
                    } else {
                        if (mTouchEvent != null && mGadgetEvent != null)
                            LOGI(TAG, "Touch event : " + mTouchEvent.action
                                    + " Gadget event : " + mGadgetEvent.action);
                        recordHistory(true);
                    }

                    // Set both to null again
                    mTouchEvent = null;
                    mGadgetEvent = null;
                }
            };
            mHandlerTouch.postDelayed(r, GADGET_EVENT_DELAY);

        }

    }

    public synchronized static void sendGadgetEvent(int action, long timestamp) {
        mGadgetEvent = new Event(action, timestamp);

        if (mTouchEvent == null) {

            // and see if the touch event is set in next 500 ms
            mHandlerGadget = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    if (mTouchEvent == null) {
                        LOGE(TAG, "No corresponding touch event");
                        recordHistory(false);
                    } else {
                        if (mTouchEvent != null && mGadgetEvent != null)
                            LOGI(TAG, "Touch event : " + mTouchEvent.action
                                    + " Gadget event : " + mGadgetEvent.action);
                        recordHistory(true);
                    }

                    // Set both to null again
                    mTouchEvent = null;
                    mGadgetEvent = null;
                }
            };
            mHandlerGadget.postDelayed(r, TOUCH_EVENT_DELAY);
        }
    }

    /**
     * Records all the match and mismatch over time. This will be
     * used for calculating the error rate.
     *
     * @param match True if events match. False otherwise
     */
    static void recordHistory(Boolean match) {
        int val = (match) ? 1 : 0;
        mHistory.add(val);
        if (mHistory.size() > HISTORY_SIZE)
            mHistory.remove(0);

        int hits = 0;
        for (int i = 0; i < mHistory.size(); i++)
            hits += mHistory.get(i);

        float curHitRate = (hits * 100) / HISTORY_SIZE;//maxHitValue;
        mMaxHitRate = (curHitRate > mMaxHitRate) ? curHitRate : mMaxHitRate;
        LOGD(TAG, "Hit rate : " + curHitRate);

        // Notify listener about hitrate
        if (hitrateChangeListener != null)
            hitrateChangeListener.onHitrateUpdate(curHitRate);

        /**
         * Send lock commands only if hit rate has crossed some threshold in history.
         * This will ensure that locking won't occur in the initial setup
         */
        if (mMaxHitRate > MIN_MAX_HITRATE_LOCK && curHitRate < MIN_HITRATE_LOCK)
            AccessController.lockDevice(mContext);
    }
}
