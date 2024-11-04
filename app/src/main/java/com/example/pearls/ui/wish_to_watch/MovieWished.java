package com.example.pearls.ui.wish_to_watch;

public class MovieWished {
    private String title;
    private int imageResourceId;
    private String intro;

    public MovieWished(String title, int imageResourceId, String intro) {
        this.title = title;
        this.imageResourceId = imageResourceId;
        this.intro = intro;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getIntro() {
        return intro;
    }
}