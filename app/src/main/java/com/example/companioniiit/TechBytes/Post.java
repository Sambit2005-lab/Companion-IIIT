package com.example.companioniiit.TechBytes;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private String id;
    private String postedBy;
    private String caption;
    private String imageUrl;
    private long timestamp;
    private int likeCount;
    private int smileyCount;
    private int thumbsUpCount;

    public Map<String, String> getUserReactions() {
        return userReactions;
    }

    public void setUserReactions(Map<String, String> userReactions) {
        this.userReactions = userReactions;
    }

    private Map<String, String> userReactions = new HashMap<>();


    public String getUserReaction() {
        return userReaction;
    }

    public void setUserReaction(String userReaction) {
        this.userReaction = userReaction;
    }

    private String userReaction="";

    // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    public Post() {
    }

    // Getters and setters for each field
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getSmileyCount() {
        return smileyCount;
    }

    public void setSmileyCount(int smileyCount) {
        this.smileyCount = smileyCount;
    }

    public int getThumbsUpCount() {
        return thumbsUpCount;
    }

    public void setThumbsUpCount(int thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }
}
