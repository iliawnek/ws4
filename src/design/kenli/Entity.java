package design.kenli;

import java.util.Date;
import java.util.HashMap;

class Entity {
    private String name;
    private HashMap<String, Cluster> clusters;

    Entity(String name) {
        this.name = name;
        this.clusters = new HashMap<>();
    }

    void addTweet(String clusterId, String tweetId, Date timestamp, String userId, String tokens, String content) {
        Cluster cluster = getCluster(clusterId);
        cluster.addTweet(tweetId, timestamp, userId, tokens, content);
    }

    private Cluster getCluster(String clusterId) {
        if (clusters.containsKey(clusterId)) {
            return clusters.get(clusterId);
        }
        Cluster newCluster = new Cluster(clusterId);
        clusters.put(clusterId, newCluster);
        return newCluster;
    }
}
