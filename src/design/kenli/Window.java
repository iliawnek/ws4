package design.kenli;

public class Window {
    private long start;
    private long end;
    private int tweetCount;
    private boolean peaking = false;

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

    public void markAsPeaking() {
        this.peaking = true;
    }

    public boolean isPeaking() {
        return this.peaking;
    }

    int getTweetCount() {
        return tweetCount;
    }

    void incrementTweetCount() {
        tweetCount++;
    }
}
