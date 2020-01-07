package com.jndcb.jndcbtwitter.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tweet {

    @SerializedName("created_at")
    @Expose
    String tweetBy;
    @SerializedName("text")
    @Expose
    String tweetText;

    public Tweet(String tweetBy, String tweet) {
        this.tweetBy = tweetBy;
        this.tweetText = tweet;
    }

    public String getTweetBy() {
        return tweetBy;
    }

    public void setTweetBy(String tweetBy) {
        this.tweetBy = tweetBy;
    }

    public String getTweet() {
        return tweetText;
    }

    public void setTweet(String tweet) {
        this.tweetText = tweet;
    }
}
