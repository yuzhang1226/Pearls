package com.example.pearls.ui.watched_list;

public class Movie {
    private String title;
    private int imageResourceId;
    private float rating;
    private String review;

    public Movie(String title, int imageResourceId, float rating, String review) {
        this.title = title;
        this.imageResourceId = imageResourceId;
        this.rating = rating;
        this.review = review;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public float getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}