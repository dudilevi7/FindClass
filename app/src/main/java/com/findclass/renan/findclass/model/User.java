package com.findclass.renan.findclass.model;

public class User {

    private String id;
    private String imageURL;
    private String institute;
    private String status;
    private String username;

    public User(String id, String username, String imageURL, String status ,String institute) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.institute = institute;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

}
