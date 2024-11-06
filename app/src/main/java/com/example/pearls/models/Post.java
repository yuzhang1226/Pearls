package com.example.pearls.models;

import com.google.firebase.firestore.DocumentSnapshot;

public class Post {
    private String postId;
    private String content;
    private String userId;
    private long timestamp;

    public Post(DocumentSnapshot document) {
        this.postId = document.getId();
        this.content = document.getString("content");
        this.userId = document.getString("userId");
        this.timestamp = document.getLong("timestamp");
    }

    // Getters for post properties
    public String getPostId() {
        return postId;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
