package li.ken;

// Represents a single tweet.
// Doesn't necessarily represent a unique tweet; there can be multiple tweets with the same content but different entity/cluster.
class Tweet {
    private String id;
    private long time;
    private String userId;
    private String tokens;
    private String content;
    private Cluster cluster;

    Tweet(String tweetId, long time, String userId, String tokens, String content, Cluster cluster) {
        this.id = tweetId;
        this.time = time;
        this.userId = userId;
        this.tokens = tokens;
        this.content = content;
        this.cluster = cluster;
    }

    String getId() {
        return id;
    }

    long getTime() {
        return time;
    }

    String getUserId() {
        return userId;
    }

    String getTokens() {
        return tokens;
    }

    String getContent() {
        return content;
    }

    // Returns the parent cluster of this tweet.
    public Cluster getCluster() {
        return cluster;
    }

    // Returns the parent entity of this tweet.
    public Entity getEntity() {
        return cluster.getEntity();
    }

    // Returns a CSV representation of this tweet as a string.
    public String toCSV() {
        return
            cluster.getId() + "," +
            cluster.getEntity().getName() + "," +
            id + ",";
    }
}
