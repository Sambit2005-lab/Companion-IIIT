package com.example.companioniiit.attendance;

import com.example.companioniiit.attendance.Subject_Item;

public class SubjectWithKey {
    private String key;
    private Subject_Item subjectItem;
    private int presentCount;
    private int absentCount;
    private int canceledCount;

    public SubjectWithKey(String key, Subject_Item subjectItem, int presentCount, int absentCount, int canceledCount) {
        this.key = key;
        this.subjectItem = subjectItem;
        this.presentCount = presentCount;
        this.absentCount = absentCount;
        this.canceledCount = canceledCount;
    }

    public SubjectWithKey(String key, Subject_Item subjectItem) {
        this.key = key;
        this.subjectItem = subjectItem;
        this.presentCount = 0;
        this.absentCount = 0;
        this.canceledCount = 0;
    }

    public String getKey() {
        return key;
    }

    public Subject_Item getSubjectItem() {
        return subjectItem;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public int getCanceledCount() {
        return canceledCount;
    }
}


