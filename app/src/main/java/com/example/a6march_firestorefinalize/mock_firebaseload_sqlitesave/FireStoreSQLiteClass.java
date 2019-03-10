package com.example.a6march_firestorefinalize.mock_firebaseload_sqlitesave;

import android.net.Uri;

public class FireStoreSQLiteClass {

    private String name;
    private String phone;
    private String my_score_ref;
    private String image_url;

    public FireStoreSQLiteClass() {

    }

    public FireStoreSQLiteClass(String name, String phone, String my_score_ref, String image_url) {
        this.name = name;
        this.phone = phone;
        this.my_score_ref = my_score_ref;
        this.image_url = image_url;
    }

    public FireStoreSQLiteClass(String name, String phone, String my_score_ref) {
        this.name = name;
        this.phone = phone;
        this.my_score_ref = my_score_ref;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMy_score_ref() {
        return my_score_ref;
    }

    public void setMy_score_ref(String my_score_ref) {
        this.my_score_ref = my_score_ref;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
