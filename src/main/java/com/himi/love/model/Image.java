package com.himi.love.model;

import java.io.Serializable;

public class Image implements Serializable {
    private Integer imageID;
    private String imageURL;
    private Integer postID;
    private Integer orderIndex;

    // 构造函数
    public Image() {}

    public Image(String imageURL, Integer postID) {
        this.imageURL = imageURL;
        this.postID = postID;
    }

    // Getter和Setter方法

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }
}