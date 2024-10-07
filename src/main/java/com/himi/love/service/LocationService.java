package com.himi.love.service;

import com.himi.love.model.Location;
import java.util.List;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public interface LocationService {
    Location createLocation(Location location);
    Location getLocationById(Integer locationId);
    List<Location> getAllLocations();
    Location updateLocation(Integer locationId, Location location);
    void deleteLocation(Integer locationId);
    List<Location> searchNearbyLocations(double latitude, double longitude, double radiusInKm);
    Location createOrGetLocation(BigDecimal latitude, BigDecimal longitude, String locationName);
}