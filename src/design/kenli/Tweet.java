package design.kenli;

import java.util.Date;

class Tweet {
    private String id;
    private Date timestamp;
    private String userId;
    private String tokens;
    private String content;
    private Cluster cluster;

    Tweet(String tweetId, Date timestamp, String userId, String tokens, String content, Cluster cluster) {
        this.id = tweetId;
        this.timestamp = timestamp;
        this.userId = userId;
        this.tokens = tokens;
        this.content = content;
        this.cluster = cluster;
    }

    String getId() {
        return id;
    }

    Date getTimestamp() {
        return timestamp;
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

    public Cluster getCluster() {
        return cluster;
    }

    public Entity getEntity() {
        return cluster.getEntity();
    }

    public String toCSV() {
        return
            cluster.getId() + "," +
            cluster.getEntity().getName() + "," +
            id + "," +
            timestamp.getTime() + "," +
            userId + "," +
            tokens + "," +
            content + "\n";
    }
}
