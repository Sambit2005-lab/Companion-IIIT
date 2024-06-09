package com.example.companioniiit;

public class SubjectWithKey {
    private String key;
    private Subject_Item subjectItem;

    public SubjectWithKey(String key, Subject_Item subjectItem) {
        this.key = key;
        this.subjectItem = subjectItem;
    }

    public String getKey() {
        return key;
    }

    public Subject_Item getSubjectItem() {
        return subjectItem;
    }
}

