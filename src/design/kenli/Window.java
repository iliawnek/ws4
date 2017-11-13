package design.kenli;

public class Window {
    private long start;
    private long end;
    private int tweetCount;
    private boolean bursting = false;

    public Window(long start, long end) {
        this.start = start;
        this.end = end;
        tweetCount = 0;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public void markAsBursting() {
        this.bursting = true;
    }

    public boolean isBursting() {
        return this.bursting;
    }

    int getTweetCount() {
        return tweetCount;
    }

    void incrementTweetCount() {
        tweetCount++;
    }
}
