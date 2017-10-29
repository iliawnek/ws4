package design.kenli;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

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
        Date timestamp = new Date(Long.parseLong(tweetValues[3]));
        String userId = tweetValues[4];
        String tokens = tweetValues[5];
        String content = tweetValues[6];

        // Add tweet to the entity.
        Entity entity = getEntity(entityName);
        entity.addTweet(clusterId, tweetId, timestamp, userId, tokens, content);

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
        Entity newEntity = new Entity(entityName);
        entities.put(entityName, newEntity);
        return newEntity;
    }

    Collection<Entity> getEntities() {
        return entities.values();
    }

    ArrayList<String> toCSV() {
        ArrayList<String> lines = new ArrayList<>();
        for (Entity entity : entities.values()) {
            lines.addAll(entity.toCSV());
        }
        return lines;
    }

    int countTweets() {
        int count = 0;
        for (Entity entity : entities.values()) {
            for (Cluster cluster : entity.getClusters()) {
                count += cluster.getTweets().size();
            }
        }
        return count;
    }
}
