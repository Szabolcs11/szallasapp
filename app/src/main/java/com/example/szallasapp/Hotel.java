package com.example.szallasapp;

public class Hotel {
    private String name;
    private String description;
    private String imageUrl;
    private String location;
    private String price;
    private String uid;
    private String userId;

    public Hotel() {
        // Default constructor required for Firestore
    }

    public Hotel(String name, String description, String imageUrl, String location, String price) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.location = location;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
