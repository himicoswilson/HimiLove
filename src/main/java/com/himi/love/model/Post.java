package com.himi.love.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Post implements Serializable {
    private Integer postID;
    private String content;
    private Integer userID;
    private Integer coupleID;
    private Integer locationID;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;

    // 构造函数
    public Post() {}

    public Post(String content, Integer userID, Integer coupleID, Integer locationID) {
        this.content = content;
        this.userID = userID;
        this.coupleID = coupleID;
        this.locationID = locationID;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    // Getter和Setter方法
    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getCoupleID() {
        return coupleID;
    }

    public void setCoupleID(Integer coupleID) {
        this.coupleID = coupleID;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}