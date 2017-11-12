package design.kenli;

import java.util.ArrayList;

public class Event {
    private long startTime;
    private long endTime;
    private ArrayList<Cluster> clusters;

    public Event() {
        this.clusters = new ArrayList<>();
    }
}
