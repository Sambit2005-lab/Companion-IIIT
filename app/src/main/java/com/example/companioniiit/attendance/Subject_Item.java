package com.example.companioniiit.attendance;

public class Subject_Item  {
    private String subjectName;
    private String teacherName;

    public Subject_Item() {
        // Default constructor required for calls to DataSnapshot.getValue(Subject_Item.class)
    }

    public Subject_Item(String subjectName, String teacherName) {
        this.subjectName = subjectName;
        this.teacherName = teacherName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}

