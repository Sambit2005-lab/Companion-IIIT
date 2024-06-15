package com.example.companioniiit.StudyFragment;

import com.example.companioniiit.StudyFragment.Note;

import java.util.List;

public class Category {
    private String title;
    private List<Note> noteList;

    public Category() {
        // Default constructor required for calls to DataSnapshot.getValue(Category.class)
    }

    public Category(String title, List<Note> noteList) {
        this.title = title;
        this.noteList = noteList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }
}
