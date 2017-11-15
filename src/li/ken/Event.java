package li.ken;

import java.util.ArrayList;

// Represents a single event.
// Contains a number of clusters of tweets related to the event.
public class Event {
    private long startTime;
    private long endTime;
    private ArrayList<Cluster> clusters;

    public Event() {
        this.clusters = new ArrayList<>();
    }
}
