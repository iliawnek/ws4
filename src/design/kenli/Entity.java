package design.kenli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class Entity {
    private String name;
    private HashMap<Integer, Cluster> clusters;
    private Dataset dataset;

    Entity(String name, Dataset dataset) {
        this.name = name;
        this.clusters = new HashMap<>();
        this.dataset = dataset;
    }

    String getName() {
        return name;
    }

    ArrayList<Cluster> getClusters() {
        return new ArrayList<>(clusters.values());
    }

    int size() {
        int size = 0;
        for (Cluster c : getClusters()) {
            size += c.size();
        }
        return size;
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

    /**
     * @param windowDuration duration of time window in minutes
     * @return list of windows across the entity
     */
    ArrayList<Window> getWindows(int windowDuration, int threshold, int filterSize, int minimumPeakSize) {
        long windowDurationMillis = Utilities.minutesToMillis(windowDuration);
        long start = getEarliestTime();
        long end = start + windowDurationMillis;
        long limit = getLatestTime();
        ArrayList<Window> windows = new ArrayList<>();

        // for each window...
        while (end < limit) {
            // create new Window
            Window window = new Window(start, end);
            windows.add(window);
            // count tweets within window
            for (Tweet tweet : getTweets()) {
                long time = tweet.getTime();
                if (time >= start && time < end) {
                    window.incrementTweetCount();
                }
            }
            // update start and end
            start = end;
            end = start + windowDurationMillis;
        }


        // identify peaking windows
        int windowCount = windows.size();
        for (int i = 0; i < windowCount; i++) {
            Window window = windows.get(i);
            int windowSize = window.getTweetCount();
            if (windowSize >= minimumPeakSize) {
                window.markAsPeaking();
                continue;
            }
            List<Double> filter = windows.subList(i < filterSize ? 0 : i - filterSize, i).stream()
                    .map(w -> (double) w.getTweetCount())
                    .collect(Collectors.toList());
            double mean = Utilities.mean(filter);
            double stdDev = Utilities.standardDeviation(filter, mean);

            if (Math.abs(windowSize - mean) > threshold * stdDev) {
                window.markAsPeaking();
            }
        }

        return windows;
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

    long getDuration() {
        return getLatestTime() - getEarliestTime();
    }

    int countTweets() {
        return getTweets().size();
    }

    void remove() {
        dataset.removeEntity(name);
    }
}
