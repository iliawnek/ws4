package li.ken;

import java.util.*;
import java.util.stream.Collectors;

// Represents a collection of tweets organised into entity partitions and clusters.
// Provides useful functions for manipulating and accessing these tweets.
class Dataset {
    private HashMap<String, Entity> entities;

    Dataset() {
        entities = new HashMap<>();
    }

    // Add a new tweet to the dataset from a CSV string representation.
    void addTweet(String tweetLine) {
        // Extract values from comma-separated line representing a tweet.
        String[] tweetValues = tweetLine.split(",", 7);
        int clusterId = Integer.parseInt(tweetValues[0]);
        String entityName = tweetValues[1];
        String tweetId = tweetValues[2];
        Long time = Long.parseLong(tweetValues[3]);
        String userId = tweetValues[4];
        String tokens = tweetValues[5];
        String content = tweetValues[6];

        // Add tweet to the entity.
        Entity entity = getEntity(entityName);
        entity.addTweet(clusterId, tweetId, time, userId, tokens, content);

    }

    // Returns an entity if it already exists.
    // Creates a new entity if it doesn't exist.
    Entity getEntity(String entityName) {
        if (entities.containsKey(entityName)) {
            return entities.get(entityName);
        }
        Entity newEntity = new Entity(entityName, this);
        entities.put(entityName, newEntity);
        return newEntity;
    }

    // Get a random entity from the dataset.
    // Useful for quick testing.
    Entity getRandomEntity() {
        Random random = new Random();
        return getEntities().get(random.nextInt(getEntities().size()));
    }

    // Get all entities in the dataset as a list.
    ArrayList<Entity> getEntities() {
        return new ArrayList<>(entities.values());
    }

    // Get all clusters in the dataset as a list.
    ArrayList<Cluster> getClusters() {
        ArrayList<Cluster> clusters = new ArrayList<>();
        for (Entity entity : getEntities()) {
            clusters.addAll(entity.getClusters());
        }
        return clusters;
    }

    // Get all tweets in the dataset as a list.
    ArrayList<Tweet> getTweets() {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (Cluster cluster : getClusters()) {
            tweets.addAll(cluster.getTweets());
        }
        return tweets;
    }

    // Remove all clusters from the dataset with fewer than "minTweets" tweets.
    void filterClusterSize(int minTweets) {
        for (Cluster cluster : getClusters()) {
            if (cluster.size() < minTweets) {
                cluster.remove();
            }
        }
    }

    // Remove all entities from the dataset with fewer than "minTweets" tweets.
    void filterEntitySize(int minTweets) {
        for (Entity entity : getEntities()) {
            if (entity.size() < minTweets) {
                entity.remove();
            }
        }
    }

    // Remove all entities from the dataset with a user diversity score below "minScore".
    void filterClusterUserDiversity(double minScore) {
        for (Cluster cluster : getClusters()) {
            if (cluster.getUserDiversity() < minScore) {
                cluster.remove();
            }
        }
    }

    // Remove all entities from the dataset with a shorter duration than "minMinutes" minutes.
    void filterEntityDuration(int minMinutes) {
        for (Entity entity : getEntities()) {
            if (entity.getDuration() < Utilities.minutesToMillis(minMinutes)) {
                entity.remove();
            }
        }
    }

    // Mark clusters in the dataset as bursting depending on the input parameters.
    // A cluster is marked as bursting if it occurs during or soon after a bursting window.
    // Can be called multiple times to capture multiple window sizes.
    //
    // windowDuration: The length in minutes of a window.
    // threshold: How much larger a window has to be compared to the mean of preceding windows to be considered bursting. Units are the number of standard deviations of the preceding windows.
    // filterSize: How many of the preceding windows should be considered when determining whether a window has burst.
    // leniency: How much later a cluster can be from a window to be considered to be caused by that bursting window. Units are the number of window durations.
    // minimumBurstFactor: If a window has more than (windowDuration * minimumBurstFactor) tweets, it is considered bursting regardless of preceding windows.
    void markBurstingClusters(int windowDuration, int threshold, int filterSize, int leniency, int minimumBurstFactor) {
        for (Entity entity : getEntities()) {
            // Get bursting windows for the entity.
            ArrayList<Window> windows = entity.getWindows(windowDuration, threshold, filterSize, minimumBurstFactor);
            List<Window> burstingWindows = windows.stream()
                    .filter(Window::isBursting)
                    .collect(Collectors.toList());

            // Mark clusters that start within or soon after the bursting windows.
            for (Cluster c : entity.getClusters()) {
                for (Window w : burstingWindows) {
                    double clusterTime = c.getCentroidTime();
                    long windowDurationMillis = Utilities.minutesToMillis(windowDuration);
                    if (clusterTime >= w.getStart() &&
                        clusterTime <= (w.getEnd() + (windowDurationMillis * leniency))) {
                        c.markAsBursting();
                    }
                }
            }
        }
    }

    // Remove all clusters from the dataset that are not marked as bursting.
    void filterNonBurstingClusters() {
        for (Cluster c : getClusters()) {
            if (!c.isBursting()) c.remove();
        }
    }

    // Get all events in the dataset.
    ArrayList<Event> getEvents() {
        // Generate an event for each cluster in the dataset.
        ArrayList<Event> events = new ArrayList<>();
        for (Cluster c : getClusters()) {
            events.add(new Event(c));
        }
        return events;
    }

    // Output the entire dataset as a list of CSV strings.
    // Sorted by cluster ID.
    ArrayList<String> toCSV() {
        // Sort clusters by ID.
        ArrayList<Cluster> clusters = getClusters();
        clusters.sort(Comparator.comparingInt(Cluster::getId));

        // Produce list of CSV strings.
        ArrayList<String> lines = new ArrayList<>();
        for (Cluster c : clusters) {
            lines.addAll(c.toCSV());
        }
        return lines;
    }

    // Return the total number of tweets in the dataset.
    int size() {
        int count = 0;
        for (Entity entity : getEntities()) {
            count += entity.size();
        }
        return count;
    }

    // Remove an entity from the dataset.
    void removeEntity(String entityName) {
        entities.remove(entityName);
    }
}
