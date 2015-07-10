package xyz.praveen.iring.touchpattern;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import xyz.praveen.iring.util.Globals;

import static java.lang.StrictMath.abs;
import static xyz.praveen.iring.util.LogUtils.LOGD;
import static xyz.praveen.iring.util.LogUtils.LOGI;
import static xyz.praveen.iring.util.LogUtils.makeLogTag;

public class TouchHandler implements View.OnTouchListener {
    final static String TAG = makeLogTag(TouchHandler.class);

    Context mContext;
    OnTouchActionListener mOnTouchActionListener;

    public final int MOVEMENT_UP = Globals.MOVEMENT_UP;
    public final int MOVEMENT_RIGHT = Globals.MOVEMENT_RIGHT;
    public final int MOVEMENT_DOWN = Globals.MOVEMENT_DOWN;
    public final int MOVEMENT_LEFT = Globals.MOVEMENT_LEFT;
    public final int MOVEMENT_PRESS = Globals.MOVEMENT_PRESS;


    private float xAxe = 0f;
    private float yAxe = 0f;
    private float dx = 0f;
    private float dy = 0f;

    /**
     * An instance of TouchHandler. This must be sent on a view ideally spanning the whole screen
     *
     * @param context       Context
     * @param touchListener Listener to receive callback for touch events
     */
    public TouchHandler(Context context, OnTouchActionListener touchListener) {
        this.mContext = context;
        this.mOnTouchActionListener = touchListener;
    }

    float lastXaxis = 0f;
    float lastYaxis = 0f;

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {

        final int actionPerformed = motionEvent.getAction();
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            float xDown = motionEvent.getX();
            float yDown = motionEvent.getY();
            lastXaxis = xDown;
            lastYaxis = yDown;
            /*LOGD(TAG, "xDown is : " + xDown);
            LOGD(TAG, "yDown is : " + yDown);*/
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float xRelease = motionEvent.getX();
            float yRelease = motionEvent.getY();
            dx = xRelease - lastXaxis;
            dy = yRelease - lastYaxis;

           /* LOGD(TAG, "dx is : " + dx);
            LOGD(TAG, "dy is : " + dy);
            LOGD(TAG, "last Yaxis is : " + lastYaxis);
            LOGD(TAG, "xRelease is  : " + xRelease);
            LOGD(TAG, "yRelease is  : " + yRelease);*/
        }


        if (dy < 0 & abs(dy) > abs(dx))
            SendEventToCallback(MOVEMENT_UP);
        if (dy > 0 & abs(dy) > abs(dx))
            SendEventToCallback(MOVEMENT_DOWN);
        if (dx > 0 & abs(dx) > abs(dy))
            SendEventToCallback(MOVEMENT_RIGHT);
        if (dx < 0 & abs(dx) > abs(dy))
            SendEventToCallback(MOVEMENT_LEFT);
        if (dx == 0 & dy == 0)
            SendEventToCallback(MOVEMENT_PRESS);

        return false;

    }


    /**
     * A callback will be made when ever a Touch event occurs
     */
    private void SendEventToCallback(int action) {
        if (mOnTouchActionListener != null)
            mOnTouchActionListener.onTouchAction(action);
    }


}



