package com.himi.love.dto;

import java.io.Serializable;

public class TagDTO implements Serializable {
    private Integer tagID; // 标签的唯一标识符
    private String tagName; // 标签的名称
    private Integer creatorID; // 创建该标签的用户ID

    // Getters and Setters
    public Integer getTagID() {
        return tagID;
    }

    public void setTagID(Integer tagID) {
        this.tagID = tagID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }
}