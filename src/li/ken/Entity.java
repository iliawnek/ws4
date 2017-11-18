package li.ken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

// Represents an entity partition.
// Consists of a number of clusters of tweets.
class Entity {
    private String name;
    private HashMap<Integer, Cluster> clusters;
    private Dataset dataset; // The parent dataset this entity is contained within.

    Entity(String name, Dataset dataset) {
        this.name = name;
        this.clusters = new HashMap<>();
        this.dataset = dataset;
    }

    // Returns the name of the entity.
    String getName() {
        return name;
    }

    // Returns all clusters in the entity as a list.
    ArrayList<Cluster> getClusters() {
        return new ArrayList<>(clusters.values());
    }

    // Returns the total number of tweets in the entity.
    int size() {
        int size = 0;
        for (Cluster c : getClusters()) {
            size += c.size();
        }
        return size;
    }

    // Returns a cluster if it already exists.
    // Creates a new cluster if it doesn't exist.
    private Cluster getCluster(int clusterId) {
        if (clusters.containsKey(clusterId)) {
            return clusters.get(clusterId);
        }
        Cluster newCluster = new Cluster(clusterId, this);
        clusters.put(clusterId, newCluster);
        return newCluster;
    }

    // Removes a cluster from the entity (and by extension the parent dataset).
    void removeCluster(int clusterId) {
        clusters.remove(clusterId);
    }

    // Gets all tweets in the entity as a list.
    ArrayList<Tweet> getTweets() {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (Cluster cluster : clusters.values()) {
            tweets.addAll(cluster.getTweets());
        }
        return tweets;
    }

    // Returns all tweets in the entity as a list of CSV strings.
    ArrayList<String> toCSV() {
        ArrayList<String> lines = new ArrayList<>();
        for (Cluster cluster : clusters.values()) {
            lines.addAll(cluster.toCSV());
        }
        return lines;
    }

    // Adds a new tweet to the corresponding cluster within the entity.
    void addTweet(int clusterId, String tweetId, long time, String userId, String tokens, String content) {
        Cluster cluster = getCluster(clusterId);
        cluster.addTweet(tweetId, time, userId, tokens, content);
    }

    // Returns an ordered list of windows of tweets mentioning the entity.
    // Marks windows as bursting depending on the input parameters.
    // See li.ken.Dataset#markBurstingClusters for information about the input parameters.
    ArrayList<Window> getWindows(int windowDuration, double threshold, int filterSize, double minimumBurstFactor) {
        long windowDurationMillis = Utilities.minutesToMillis(windowDuration);
        long start = getEarliestTime();
        long end = start + windowDurationMillis;
        long limit = getLatestTime();
        ArrayList<Window> windows = new ArrayList<>();

        // For each window...
        while (end < limit) {
            // Create a new Window object.
            Window window = new Window(start, end);
            windows.add(window);

            // Count tweets within the window.
            for (Tweet tweet : getTweets()) {
                long time = tweet.getTime();
                if (time >= start && time < end) {
                    window.incrementTweetCount();
                }
            }

            // Update start and end for the next iteration.
            start = end;
            end = start + windowDurationMillis;
        }

        // Identify bursting windows.
        int windowCount = windows.size();
        for (int i = 0; i < windowCount; i++) {
            Window window = windows.get(i);
            int windowSize = window.getTweetCount();

            // Mark window as bursting if window is sufficiently big, regardless of preceding windows.
            if (windowSize >= windowDuration * minimumBurstFactor) {
                window.markAsBursting();
                continue;
            }

            // Compare current window with statistics about preceding windows to determining bursting status.
            List<Double> filter = windows.subList(i < filterSize ? 0 : i - filterSize, i).stream()
                    .map(w -> (double) w.getTweetCount())
                    .collect(Collectors.toList());
            double mean = Utilities.mean(filter);
            double stdDev = Utilities.standardDeviation(filter, mean);
            if (Math.abs(windowSize - mean) > (threshold * stdDev)) {
                window.markAsBursting();
            }
        }

        return windows;
    }

    // Get the timestamp of the earliest tweet mentioning the entity.
    long getEarliestTime() {
        long earliest = Long.MAX_VALUE;
        for (Tweet tweet : getTweets()) {
            long time = tweet.getTime();
            if (time < earliest) earliest = time;
        }
        return earliest;
    }

    // Get the timestamp of the latest tweet mentioning the entity.
    long getLatestTime() {
        long latest = Long.MIN_VALUE;
        for (Tweet tweet : getTweets()) {
            long time = tweet.getTime();
            if (time > latest) latest = time;
        }
        return latest;
    }

    // Get the overall duration of the entity (from first tweet to last) in milliseconds.
    long getDuration() {
        return getLatestTime() - getEarliestTime();
    }

    // Remove this entity from the parent dataset.
    void remove() {
        dataset.removeEntity(name);
    }
}
