package com.example.companioniiit.MyCalendar;

public class eventHoliday {
    private String date;
    private String description;

    public eventHoliday() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public eventHoliday(String date, String description) {
        this.date = date;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}