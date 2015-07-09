package xyz.praveen.iring.accesscontrol;

import java.util.ArrayList;

import xyz.praveen.iring.model.Event;

/**
 * Takes events from Gadgeteer, Touch screen and performs matching.
 * <p/>
 * Created by praveen on 9/7/15.
 */
public class EventBox {
    static ArrayList<Event> mTouchEvents = new ArrayList<Event>();
    static ArrayList<Event> mGadgetEvents = new ArrayList<Event>();

    /**
     * Send a touch event to the event box
     *
     * @param action Action code.
     */
    synchronized static void sendTouchEvent(int action) {
        mTouchEvents.add(new Event(action, System.currentTimeMillis()));
    }

    synchronized static void sendGadgetEvent(int action, long timestamp) {
        mGadgetEvents.add(new Event(action, timestamp));
        // -TODO- Checking for matches now.
    }
}
