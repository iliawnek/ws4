package li.ken;

// Represents a window of tweets within an entity.
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

    // Marks this window as bursting.
    public void markAsBursting() {
        this.bursting = true;
    }

    // Returns true if this window is bursting.
    public boolean isBursting() {
        return this.bursting;
    }

    // Returns the number of tweets that lie within this window.
    int getTweetCount() {
        return tweetCount;
    }

    // Increase the interal counter of tweets within this window.
    void incrementTweetCount() {
        tweetCount++;
    }
}
