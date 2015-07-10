package xyz.praveen.iring.accesscontrol;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import xyz.praveen.iring.model.Event;

import static xyz.praveen.iring.util.LogUtils.LOGD;
import static xyz.praveen.iring.util.LogUtils.LOGI;
import static xyz.praveen.iring.util.LogUtils.makeLogTag;

/**
 * Takes events from Gadgeteer, Touch screen and performs matching.
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
     * Size of action history over which error rate is calculated
     */
    static final int HISTORY_SIZE = 10;

    static List<Integer> mHistory = new ArrayList<>(10);

    static Handler handler;
    static Event mTouchEvent;
    static Event mGadgetEvent;

    /**
     * Send a touch event to the event box
     *
     * @param action Action code.
     */
    public synchronized static void sendTouchEvent(int action) {
        //mTouchEvents.add(new Event(action, System.currentTimeMillis()));

        mTouchEvent = new Event(action, System.currentTimeMillis());

        // Set gadget event to null
        mGadgetEvent = null;

        // and see if the gadget event is set in next 50 ms
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                if (mGadgetEvent == null) {
                    LOGI(TAG, "No corresponding gadget event");
                    recordHistory(false);
                } else {
                    LOGI(TAG, "Touch event : " + mTouchEvent.action
                            + " Gadget event : " + mGadgetEvent.action);
                    recordHistory(true);
                }

                // Set both to null again
                mTouchEvent = null;
                mGadgetEvent = null;
            }
        };

        handler.postDelayed(r, GADGET_EVENT_DELAY);
    }

    public synchronized static void sendGadgetEvent(int action, long timestamp) {
        //mGadgetEvents.add(new Event(action, timestamp));
        mGadgetEvent = new Event(action, timestamp);

        // Check if there is a corresponding touch event. Else, foul play
        if (mTouchEvent == null) {
            LOGI(TAG, "No corresponding touch event");
            recordHistory(false);
        }

        // Else case will be taken care by the delayed runnable
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
        LOGD(TAG, "Error rate : " + (HISTORY_SIZE - hits) * 100);
    }
}
