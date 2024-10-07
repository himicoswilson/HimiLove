package com.himi.love.controller;

import com.himi.love.model.Location;
import com.himi.love.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        return ResponseEntity.ok(locationService.createLocation(location));
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<Location> getLocationById(@PathVariable Integer locationId) {
        return ResponseEntity.ok(locationService.getLocationById(locationId));
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<Location> updateLocation(@PathVariable Integer locationId, @RequestBody Location location) {
        return ResponseEntity.ok(locationService.updateLocation(locationId, location));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Integer locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Location>> getNearbyLocations(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius) {
        return ResponseEntity.ok(locationService.searchNearbyLocations(latitude, longitude, radius));
    }

    @PostMapping("/create-or-get")
    public ResponseEntity<Location> createOrGetLocation(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam String locationName) {
        return ResponseEntity.ok(locationService.createOrGetLocation(latitude, longitude, locationName));
    }
}