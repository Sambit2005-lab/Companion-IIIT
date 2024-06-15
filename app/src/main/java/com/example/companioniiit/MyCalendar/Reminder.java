package com.example.companioniiit.MyCalendar;

public class Reminder {
    public String reminderId;
    public String title;
    public String description;
    public long time;

    public Reminder() {
    }

    public Reminder(String reminderId, String title, String description, long time) {
        this.reminderId = reminderId;
        this.title = title;
        this.description = description;
        this.time = time;
    }
}

