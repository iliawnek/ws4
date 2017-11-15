package li.ken;

import java.util.ArrayList;

// Represents a single event.
// Contains a cluster of tweets related to the event.
public class Event {
    private Cluster cluster;

    public Event(Cluster cluster) {
        this.cluster = cluster;
    }
}
