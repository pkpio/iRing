package xyz.praveen.iring.touchpattern;

/**
 * For callbacks on TouchEvents
 * <p/>
 * Created by praveen on 9/7/15.
 */
public interface OnTouchListener {

    /**
     * Callback when a touch event occurs
     *
     * @param event Event code
     */
    public void onTouchEvent(int event);
}
