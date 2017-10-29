package design.kenli;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringJoiner;

public class Cluster {
    private int id;
    private ArrayList<Tweet> tweets;
    private Entity entity;

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

    void addTweet(String tweetId, Date timestamp, String userId, String tokens, String content) {
        Tweet tweet = new Tweet(tweetId, timestamp, userId, tokens, content, this);
        tweets.add(tweet);
    }

    ArrayList<String> toCSV() {
        ArrayList<String> lines = new ArrayList<>();
        for (Tweet tweet : tweets) {
            lines.add(tweet.toCSV());
        }
        return lines;
    }
}
