package design.kenli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

class Entity {
    private String name;
    private HashMap<Integer, Cluster> clusters;

    Entity(String name) {
        this.name = name;
        this.clusters = new HashMap<>();
    }

    String getName() {
        return name;
    }

    Collection<Cluster> getClusters() {
        return clusters.values();
    }

    /**
     * Returns a cluster if it already exists.
     * Creates a new cluster if it doesn't exist.
     * @param clusterId ID of the cluster.
     * @return a cluster with the ID clusterId.
     */
    private Cluster getCluster(int clusterId) {
        if (clusters.containsKey(clusterId)) {
            return clusters.get(clusterId);
        }
        Cluster newCluster = new Cluster(clusterId, this);
        clusters.put(clusterId, newCluster);
        return newCluster;
    }

    void removeCluster(int clusterId) {
        clusters.remove(clusterId);
    }

    ArrayList<Tweet> getTweets() {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (Cluster cluster : clusters.values()) {
            tweets.addAll(cluster.getTweets());
        }
        return tweets;
    }

    ArrayList<String> toCSV() {
        ArrayList<String> lines = new ArrayList<>();
        for (Cluster cluster : clusters.values()) {
            lines.addAll(cluster.toCSV());
        }
        return lines;
    }

    void addTweet(int clusterId, String tweetId, long time, String userId, String tokens, String content) {
        Cluster cluster = getCluster(clusterId);
        cluster.addTweet(tweetId, time, userId, tokens, content);
    }

    long getEarliestTime() {
        long earliest = Long.MAX_VALUE;
        for (Tweet tweet : getTweets()) {
            long time = tweet.getTime();
            if (time < earliest) earliest = time;
        }
        return earliest;
    }

    long getLatestTime() {
        long latest = Long.MIN_VALUE;
        for (Tweet tweet : getTweets()) {
            long time = tweet.getTime();
            if (time > latest) latest = time;
        }
        return latest;
    }
}
