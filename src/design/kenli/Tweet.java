package design.kenli;

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
            id + ",";
    }
}
