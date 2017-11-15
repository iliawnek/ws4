package li.ken;

import java.util.ArrayList;

// Represents a single event.
// Contains a cluster of tweets related to the event.
public class Event {
    private ArrayList<Cluster> clusters;

    public Event(Cluster cluster) {
        this.clusters = new ArrayList<>();
        this.clusters.add(cluster);
    }
}
