package com.himi.love.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class CoupleDTO implements Serializable {
    private Integer coupleID;
    private Integer userID1;
    private Integer userID2;
    private LocalDate anniversaryDate;
    private String bgImg;

    // Getter和Setter方法

    public Integer getCoupleID() {
        return coupleID;
    }

    public void setCoupleID(Integer coupleID) {
        this.coupleID = coupleID;
    }

    public Integer getUserID1() {
        return userID1;
    }

    public void setUserID1(Integer userID1) {
        this.userID1 = userID1;
    }

    public Integer getUserID2() {
        return userID2;
    }

    public void setUserID2(Integer userID2) {
        this.userID2 = userID2;
    }

    public LocalDate getAnniversaryDate() {
        return anniversaryDate;
    }

    public void setAnniversaryDate(LocalDate anniversaryDate) {
        this.anniversaryDate = anniversaryDate;
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }
    // ... (为所有字段添加getter和setter方法)
}