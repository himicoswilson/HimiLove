package com.himi.love.service.impl;

import com.himi.love.service.LocationService;
import com.himi.love.mapper.LocationMapper;
import com.himi.love.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.math.BigDecimal;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationMapper locationMapper;

    @Override
    public Location createLocation(Location location) {
        locationMapper.insert(location);
        return location;
    }

    @Override
    public Location getLocationById(Integer locationId) {
        return locationMapper.findById(locationId);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationMapper.findAll();
    }

    @Override
    public Location updateLocation(Integer locationId, Location location) {
        location.setLocationID(locationId);
        locationMapper.update(location);
        return location;
    }

    @Override
    public void deleteLocation(Integer locationId) {
        locationMapper.deleteById(locationId);
    }

    @Override
    public List<Location> searchNearbyLocations(double latitude, double longitude, double radiusInKm) {
        return locationMapper.findNearbyLocations(latitude, longitude, radiusInKm);
    }

    @Override
    public Location createOrGetLocation(BigDecimal latitude, BigDecimal longitude, String locationName) {
        Location existingLocation = locationMapper.findByLatLongAndName(latitude, longitude, locationName);
        if (existingLocation != null) {
            return existingLocation;
        }
        Location newLocation = new Location(latitude, longitude, locationName);
        createLocation(newLocation);
        return newLocation;
    }
}