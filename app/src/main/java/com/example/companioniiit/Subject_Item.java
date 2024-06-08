package com.example.companioniiit;

public class Subject_Item {

    public String getSubjectName() {
        return subjectName;
    }

    String subjectName;

    public String getTeacherName() {
        return teacherName;
    }

    String teacherName;

    public Subject_Item(String subjectName,String teacherName) {
        this.subjectName = subjectName;
        this.teacherName= teacherName;
    }



}
