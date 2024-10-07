package com.himi.love.model;

import java.io.Serializable;
import java.time.LocalDate;


public class Couple implements Serializable {
    private Integer coupleID;
    private Integer userID1;
    private Integer userID2;
    private String bgImg;
    private LocalDate anniversaryDate;

    // 构造函数、getter和setter方法
    public Couple() {}

    public Couple(Integer userID1, Integer userID2, LocalDate anniversaryDate) {
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.anniversaryDate = anniversaryDate;
    }

    // Getter和Setter方法
    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

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
}