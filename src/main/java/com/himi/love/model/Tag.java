package com.himi.love.model;

import java.io.Serializable;

public class Tag implements Serializable {
    private Integer tagID;
    private String tagName;
    private Integer creatorID;

    // 构造函数
    public Tag() {}

    public Tag(String tagName, Integer creatorID) {
        this.tagName = tagName;
        this.creatorID = creatorID;
    }

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