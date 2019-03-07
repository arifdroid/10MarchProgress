package com.example.a6march_firestorefinalize;

public class ContactUser {
    private String name;
    private String uid;
    private String phone;
    private String rating;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ContactUser() {
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
