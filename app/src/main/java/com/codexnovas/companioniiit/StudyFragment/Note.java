package com.codexnovas.companioniiit.StudyFragment;
public class Note {
    private String title;
    private String link;

    public Note() {
        // Default constructor required for calls to DataSnapshot.getValue(Note.class)
    }

    public Note(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}