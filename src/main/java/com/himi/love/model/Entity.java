package com.himi.love.model;

import java.io.Serializable;

public class Entity implements Serializable {
    private Integer entityID;
    private String entityName;
    private String entityType;
    private String avatar;
    private Integer coupleID;

    // 构造函数
    public Entity() {}

    public Entity(String entityName, String entityType, Integer coupleID) {
        this.entityName = entityName;
        this.entityType = entityType;
        this.coupleID = coupleID;
    }

    // Getter和Setter方法

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getCoupleID() {
        return coupleID;
    }

    public void setCoupleID(Integer coupleID) {
        this.coupleID = coupleID;
    }
    // ... (为所有字段添加getter和setter方法)
}