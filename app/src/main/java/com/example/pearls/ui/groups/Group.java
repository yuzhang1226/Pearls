package com.example.pearls.ui.groups;

public class Group {
    private String groupId;
    private String groupName;
    private String introduction;
    private String memberNumber;
    private String postsNumber;
    private String userId;

    public Group() {
        // Firestore requires a no-argument constructor
    }

    public Group(String groupId, String groupName, String introduction, String memberNumber, String postsNumber, String userId) {
        this.groupId = groupId; // Note: Will be auto-generated by Firestore
        this.groupName = groupName;
        this.introduction = introduction;
        this.memberNumber = memberNumber;
        this.postsNumber = postsNumber;
        this.userId = userId;
    }

    // Getters and Setters
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getPostsNumber() {
        return postsNumber;
    }

    public void setPostsNumber(String postsNumber) {
        this.postsNumber = postsNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
