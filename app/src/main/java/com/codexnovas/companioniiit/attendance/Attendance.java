package com.codexnovas.companioniiit.attendance;

public  class Attendance {
    private String date;
    private String status;

    public Attendance() {
        // Default constructor required for calls to DataSnapshot.getValue(Attendance.class)
    }

    public Attendance(String date, String status) {
        this.date = date;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
