package design.kenli;

import java.util.ArrayList;
import java.util.HashSet;

public class Cluster {
    private int id;
    private ArrayList<Tweet> tweets;
    private Entity entity;
    private boolean peaking = false;

    public Cluster(int id, Entity entity) {
        this.id = id;
        this.tweets = new ArrayList<>();
        this.entity = entity;
    }

    int getId() {
        return id;
    }

    ArrayList<Tweet> getTweets() {
        return tweets;
    }

    Entity getEntity() {
        return entity;
    }

    void addTweet(String tweetId, long time, String userId, String tokens, String content) {
        Tweet tweet = new Tweet(tweetId, time, userId, tokens, content, this);
        tweets.add(tweet);
    }

    ArrayList<String> toCSV() {
        ArrayList<String> lines = new ArrayList<>();
        for (Tweet tweet : tweets) {
            lines.add(tweet.toCSV());
        }
        return lines;
    }

    int size() {
        return tweets.size();
    }

    double getCentroidTime() {
        double sum = 0.0;
        for (Tweet tweet : getTweets()) {
            sum += tweet.getTime();
        }
        return sum/size();
    }

    long getEarliestTime() {
        long earliest = Long.MAX_VALUE;
        for (Tweet tweet : getTweets()) {
            long time = tweet.getTime();
            if (time < earliest) earliest = time;
        }
        return earliest;
    }

//    double getStandardTimeDeviation() {
//        double mean = getCentroidTime();
//        double temp = 0;
//        for (Tweet tweet : getTweets()) {
//            double time = tweet.getTime();
//            temp += (time - mean) * (time - mean);
//        }
//        double variance = temp / (size() - 1);
//        return Math.sqrt(variance);
//    }

    /**
     * Diversity of 1 indicates that every user is unique.
     * @return measure of diversity of users in the cluster
     */
    double getUserDiversity() {
        HashSet<String> userIds = new HashSet<>();
        for (Tweet tweet : getTweets()) {
            userIds.add(tweet.getUserId());
        }
        double uniqueUserIdCount = userIds.size();
        double clusterSize = size();
        return uniqueUserIdCount / clusterSize;
    }

    void remove() {
        entity.removeCluster(id);
    }

    void markAsPeaking() {
        peaking = true;
    }

    boolean isPeaking() {
        return peaking;
    }
}
