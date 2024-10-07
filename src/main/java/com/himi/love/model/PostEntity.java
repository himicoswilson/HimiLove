package com.himi.love.model;

import java.io.Serializable;

public class PostEntity implements Serializable {
    private Integer postID;
    private Integer entityID;

    // 构造函数
    public PostEntity() {}

    public PostEntity(Integer postID, Integer entityID) {
        this.postID = postID;
        this.entityID = entityID;
    }

    // Getter和Setter方法
    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }
}