package design.kenli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

    private Cluster getCluster(int clusterId) {
        if (clusters.containsKey(clusterId)) {
            return clusters.get(clusterId);
        }
        Cluster newCluster = new Cluster(clusterId, this);
        clusters.put(clusterId, newCluster);
        return newCluster;
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

    void addTweet(int clusterId, String tweetId, Date timestamp, String userId, String tokens, String content) {
        Cluster cluster = getCluster(clusterId);
        cluster.addTweet(tweetId, timestamp, userId, tokens, content);
    }
}
