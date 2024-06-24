package com.example.companioniiit.Announcement;

public class Announcement {
    private String caption;
    private String imageUrl;
    private String postedBy;
    private Long timestamp;

    public Announcement() {
        // Default constructor required for calls to DataSnapshot.getValue(Announcement.class)
    }

    public Announcement(String caption, String imageUrl, String postedBy, Long timestamp) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.postedBy = postedBy;
        this.timestamp = timestamp;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
