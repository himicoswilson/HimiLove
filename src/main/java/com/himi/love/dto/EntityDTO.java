package com.himi.love.dto;

import java.io.Serializable;

public class EntityDTO implements Serializable {
    private Integer entityID;
    private String entityName;
    private String entityType;
    private String avatar;
    private Integer coupleID;
    private boolean unviewed;

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

    public boolean isUnviewed() {
        return unviewed;
    }

    public void setUnviewed(boolean unviewed) {
        this.unviewed = unviewed;
    }
}