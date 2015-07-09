package xyz.praveen.iring.touchpattern;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.StrictMath.abs;

public class TouchHandler implements View.OnTouchListener {
    Context mContext;
    OnTouchListener mOnTouchListener;

    public final int MOVEMENT_UP = 1;
    public final int MOVEMENT_RIGHT = 2;
    public final int MOVEMENT_DOWN = 3;
    public final int MOVEMENT_LEFT = 4;
    public final int MOVEMENNT_PRESS = 5;


    private float xAxe = 0f;
    private float yAxe = 0f;
    private float dx = 0f;
    private float dy = 0f;

    public TouchHandler(Context context, OnTouchListener touchListener) {
        this.mContext = context;
        this.mOnTouchListener = touchListener;
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
            System.out.print("xDown is   " + xDown);
            System.out.print("  yDown is" + yDown);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            float xRelease = motionEvent.getX();
            float yRelease = motionEvent.getY();
            dx = xRelease - lastXaxis;
            dy = yRelease - lastYaxis;
            System.out.print("dx is   " + dx);
            System.out.print("  dy is" + dy);
            System.out.print(" lastYaxis is " + lastYaxis);
            System.out.print("xRelease is   " + xRelease);
            System.out.print(" yRelease is" + yRelease);
        }


        if (dy < 0 & abs(dy) > abs(dx)) {
            SendEventToCallback(MOVEMENT_UP);
        }
        if (dy > 0 & abs(dy) > abs(dx)) {
            SendEventToCallback(MOVEMENT_DOWN);
        }
        if (dx > 0 & abs(dx) > abs(dy))
            SendEventToCallback(MOVEMENT_RIGHT);
        if (dx < 0 & abs(dx) > abs(dy))
            SendEventToCallback(MOVEMENT_LEFT);
        if (dx == 0 & dy == 0)
            SendEventToCallback(MOVEMENNT_PRESS);

        return true;

    }


    /**
     * This callback will be called when ever a Touch event occurs
     */
    private void SendEventToCallback(int action) {
        System.out.println("The action is " + action);
        if (mOnTouchListener != null)
            mOnTouchListener.onTouchEvent(action);
    }


}



