package design.kenli;

import java.util.ArrayList;
import java.util.Date;

public class Cluster {
    private String id;
    private ArrayList<Tweet> tweets;

    public Cluster(String id) {
        this.id = id;
        this.tweets = new ArrayList<>();
    }

    void addTweet(String tweetId, Date timestamp, String userId, String tokens, String content) {
        Tweet tweet = new Tweet(tweetId, timestamp, userId, tokens, content);
        tweets.add(tweet);
    }
}
