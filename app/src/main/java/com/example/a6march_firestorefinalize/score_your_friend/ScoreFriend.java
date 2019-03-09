package com.example.a6march_firestorefinalize.score_your_friend;

public class ScoreFriend {

    private String name;
    private String image_url;

    private String my_score_ref;

    public ScoreFriend() {

    }

    public ScoreFriend(String name, String image_url, String my_score_ref) {
        this.name = name;
        this.image_url = image_url;
        this.my_score_ref = my_score_ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getMy_score_ref() {
        return my_score_ref;
    }

    public void setMy_score_ref(String my_score_ref) {
        this.my_score_ref = my_score_ref;
    }
}
