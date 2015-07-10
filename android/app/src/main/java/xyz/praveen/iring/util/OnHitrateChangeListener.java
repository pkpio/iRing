package xyz.praveen.iring.util;

/**
 * Called when ever hit rate is updated
 * Created by praveen on 10/7/15.
 */
public interface OnHitrateChangeListener {

    /**
     * Called when hitrate is updated
     *
     * @param hitrate New hitrate
     */
    void onHitrateUpdate(int hitrate);
}
