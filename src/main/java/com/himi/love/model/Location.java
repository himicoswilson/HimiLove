package com.himi.love.model;

import java.io.Serializable;
import java.math.BigDecimal;
import org.locationtech.jts.geom.Point;

public class Location implements Serializable {
    private Integer locationID;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String locationName;
    private Point coordinates;

    // 构造函数
    public Location() {}

    public Location(BigDecimal latitude, BigDecimal longitude, String locationName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
    }

    public Location(BigDecimal latitude, BigDecimal longitude, String locationName, Point coordinates) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
        this.coordinates = coordinates;
    }

    // Getter和Setter方法
    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }
}