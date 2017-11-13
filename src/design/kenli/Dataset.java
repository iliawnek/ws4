package design.kenli;

import java.util.*;
import java.util.stream.Collectors;

class Dataset {
    private HashMap<String, Entity> entities;

    Dataset() {
        entities = new HashMap<>();
    }

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

    /**
     * Returns an entity if it already exists.
     * Creates a new entity if it doesn't exist.
     * @param entityName Name of the entity.
     * @return an entity with the name entityName.
     */
    Entity getEntity(String entityName) {
        if (entities.containsKey(entityName)) {
            return entities.get(entityName);
        }
        Entity newEntity = new Entity(entityName, this);
        entities.put(entityName, newEntity);
        return newEntity;
    }

    Entity getRandomEntity() {
        Random random = new Random();
        return getEntities().get(random.nextInt(getEntities().size()));
    }

    ArrayList<Entity> getEntities() {
        return new ArrayList<>(entities.values());
    }

    ArrayList<Cluster> getClusters() {
        ArrayList<Cluster> clusters = new ArrayList<>();
        for (Entity entity : getEntities()) {
            for (Cluster cluster : entity.getClusters()) {
                clusters.add(cluster);
            }
        }
        return clusters;
    }

    ArrayList<Tweet> getTweets() {
        ArrayList<Tweet> tweets = new ArrayList<>();
        ArrayList<Cluster> clusters = getClusters();
        for (Cluster cluster : clusters) {
            tweets.addAll(cluster.getTweets());
        }
        return tweets;
    }

    void filterClusterSize(int minTweets) {
        for (Cluster cluster : getClusters()) {
            if (cluster.size() < minTweets) {
                cluster.remove();
            }
        }
    }

    void filterEntitySize(int minTweets) {
        for (Entity entity : getEntities()) {
            if (entity.size() < minTweets) {
                entity.remove();
            }
        }
    }

//    void filterClusterStandardTimeDeviation(int maxMinutes) {
//        int maxMillis = maxMinutes * 60 * 1000;
//        for (Cluster cluster : getClusters()) {
//            if (cluster.getStandardTimeDeviation() > maxMillis) {
//                cluster.remove();
//            }
//        }
//    }

    void filterClusterUserDiversity(double min) {
        for (Cluster cluster : getClusters()) {
            if (cluster.getUserDiversity() < min) {
                cluster.remove();
            }
        }
    }

    void filterEntityDuration(int minMinutes) {
        int minMillis = minMinutes * 60 * 1000;
        for (Entity entity : getEntities()) {
            if (entity.getDuration() < minMillis) {
                entity.remove();
            }
        }
    }

    void markBurstingClusters(int windowDuration, int threshold, int filterSize, int leniency, int minimumBurstFactor) {
        for (Entity entity : getEntities()) {
            // get bursting windows
            ArrayList<Window> windows = entity.getWindows(windowDuration, threshold, filterSize, minimumBurstFactor);
            List<Window> burstingWindows = windows.stream()
                    .filter(Window::isBursting)
                    .collect(Collectors.toList());

            // mark clusters that start within or close to bursting windows
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

    void filterNonBurstingClusters() {
        for (Cluster c : getClusters()) {
            if (!c.isBursting()) c.remove();
        }
    }

//    void detectEvents(int timeWindow) {
//        for (Entity entity : getEntities()) {
//            entity.detectEvents(timeWindow);
//        }
//    }

    ArrayList<String> toCSV() {
        ArrayList<String> lines = new ArrayList<>();
        for (Entity entity : getEntities()) {
            lines.addAll(entity.toCSV());
        }
        return lines;
    }

    int countTweets() {
        int count = 0;
        for (Entity entity : getEntities()) {
            for (Cluster cluster : entity.getClusters()) {
                count += cluster.getTweets().size();
            }
        }
        return count;
    }

    void removeEntity(String entityName) {
        entities.remove(entityName);
    }
}
