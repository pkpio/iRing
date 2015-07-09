package xyz.praveen.iring.model;

/**
 * A model to represent an event
 * <p/>
 * Created by praveen on 9/7/15.
 */
public class Event {
    /**
     * Code of the action occurred
     */
    public int action;

    /**
     * Time stamp of when the event occurred
     */
    public long timestamp;

    public Event(int action, long timestamp) {
        this.action = action;
        this.timestamp = timestamp;
    }
}
