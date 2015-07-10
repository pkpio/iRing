package xyz.praveen.iring.touch;

/**
 * For callbacks on TouchEvents
 * <p/>
 * Created by praveen on 9/7/15.
 */
public interface OnTouchActionListener {

    /**
     * Callback when a touch action occurs
     *
     * @param action Action code
     */
    public void onTouchAction(int action);
}
