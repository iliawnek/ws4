package li.ken;

import java.util.ArrayList;
import java.util.HashSet;

// Represents a cluster of tweets within a parent entity partition.
public class Cluster {
    private int id;
    private ArrayList<Tweet> tweets;
    private Entity entity;
    private boolean bursting = false; // All clusters are assumed not to be bursting until ken.li.Dataset#markBurstingClusters is called.

    public Cluster(int id, Entity entity) {
        this.id = id;
        this.tweets = new ArrayList<>();
        this.entity = entity;
    }

    // Get the ID of the cluster as determined by the contained tweets from the input file.
    int getId() {
        return id;
    }

    // Returns all tweets in the cluster as a list.
    ArrayList<Tweet> getTweets() {
        return tweets;
    }

    // Returns the parent entity.
    Entity getEntity() {
        return entity;
    }

    // Adds a new tweet to the cluster.
    void addTweet(String tweetId, long time, String userId, String tokens, String content) {
        Tweet tweet = new Tweet(tweetId, time, userId, tokens, content, this);
        tweets.add(tweet);
    }

    // Returns all tweets in the cluster as a list of CSV strings.
    ArrayList<String> toCSV() {
        ArrayList<String> lines = new ArrayList<>();
        for (Tweet tweet : tweets) {
            lines.add(tweet.toCSV());
        }
        return lines;
    }

    // Returns the total number of tweets in the cluster.
    int size() {
        return tweets.size();
    }

    // Returns the mean timestamp of all tweets in the cluster.
    double getCentroidTime() {
        double sum = 0.0;
        for (Tweet tweet : getTweets()) {
            sum += tweet.getTime();
        }
        return sum/size();
    }

    // Returns the earliest timestamp of any tweet in the cluster.
    long getEarliestTime() {
        long earliest = Long.MAX_VALUE;
        for (Tweet tweet : getTweets()) {
            long time = tweet.getTime();
            if (time < earliest) earliest = time;
        }
        return earliest;
    }

    // Gets the user diversity score of the cluster.
    // User diversity score is defined as the number of unique users per tweet.
    // A score of 1 would indicate that every user is unique.
    double getUserDiversity() {
        HashSet<String> userIds = new HashSet<>();
        for (Tweet tweet : getTweets()) {
            userIds.add(tweet.getUserId());
        }
        double uniqueUserIdCount = userIds.size();
        double clusterSize = size();
        return uniqueUserIdCount / clusterSize;
    }

    // Remove this cluster from the parent entity.
    void remove() {
        entity.removeCluster(id);
    }

    // Mark this cluster as bursting.
    void markAsBursting() {
        bursting = true;
    }

    // Returns true if this cluster is bursting.
    boolean isBursting() {
        return bursting;
    }
}
