package com.himi.love.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mood implements Serializable {
    private Integer moodID;
    private Integer userID;
    private String moodName;
    private String moodIcon;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isDeleted;

    // 构造函数
    public Mood() {}

    public Mood(Integer userID, String moodName, String moodIcon) {
        this.userID = userID;
        this.moodName = moodName;
        this.moodIcon = moodIcon;
        this.startTime = LocalDateTime.now();
        this.isDeleted = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMoodID() {
        return moodID;
    }

    public void setMoodID(Integer moodID) {
        this.moodID = moodID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getMoodName() {
        return moodName;
    }

    public void setMoodName(String moodName) {
        this.moodName = moodName;
    }

    public String getMoodIcon() {
        return moodIcon;
    }

    public void setMoodIcon(String moodIcon) {
        this.moodIcon = moodIcon;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}