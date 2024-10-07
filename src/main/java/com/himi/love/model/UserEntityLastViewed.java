package com.himi.love.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserEntityLastViewed implements Serializable {
    private Integer userID;
    private Integer entityID;
    private LocalDateTime lastViewedTime;

    // 构造函数、getter和setter方法

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public LocalDateTime getLastViewedTime() {
        return lastViewedTime;
    }

    public void setLastViewedTime(LocalDateTime lastViewedTime) {
        this.lastViewedTime = lastViewedTime;
    }
}