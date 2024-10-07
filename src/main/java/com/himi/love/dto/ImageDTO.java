package com.himi.love.dto;

import java.io.Serializable;

public class ImageDTO implements Serializable {
    private Integer imageID; // 图片的唯一标识符
    private String imageURL; // 图片的URL
    private Integer postID; // 关联的帖子的ID

    // Getters and Setters
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
