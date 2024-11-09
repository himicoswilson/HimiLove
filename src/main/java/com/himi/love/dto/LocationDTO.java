package com.himi.love.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class LocationDTO implements Serializable {
    private Integer locationID;
    private String locationName;
    private BigDecimal latitude;
    private BigDecimal longitude;

    // Getters and Setters
    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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
}