package com.example.a6march_firestorefinalize.mock_firebaseload_sqlitesave;

public class GettingRatingClass {

    private String name;
    private String my_score_ref;
    private float my_rating;

    public GettingRatingClass(String name, String my_score_ref, float my_rating) {
        this.name = name;
        this.my_score_ref = my_score_ref;
        this.my_rating = my_rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMy_score_ref() {
        return my_score_ref;
    }

    public void setMy_score_ref(String my_score_ref) {
        this.my_score_ref = my_score_ref;
    }

    public float getMy_rating() {
        return my_rating;
    }

    public void setMy_rating(float my_rating) {
        this.my_rating = my_rating;
    }
}
