package com.codexnovas.companioniiit.SocietyFragment;

public class TeamMember {
    private String designation;
    private String imageUrl;
    private String name;

    public TeamMember() {
        // Default constructor required for calls to DataSnapshot.getValue(TeamMember.class)
    }

    public TeamMember(String designation, String imageUrl, String name) {
        this.designation = designation;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}