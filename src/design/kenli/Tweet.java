package design.kenli;

import java.util.Date;

class Tweet {
    private String id;
    private Date timestamp;
    private String userId;
    private String tokens;
    private String content;

    Tweet(String tweetId, Date timestamp, String userId, String tokens, String content) {
        this.id = tweetId;
        this.timestamp = timestamp;
        this.userId = userId;
        this.tokens = tokens;
        this.content = content;
    }
}
