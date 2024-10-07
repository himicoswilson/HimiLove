package com.himi.love.controller;

import com.himi.love.model.Mood;
import com.himi.love.model.User;
import com.himi.love.service.MoodService;
import com.himi.love.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moods")
public class MoodController {

    @Autowired
    private MoodService moodService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createMood(@RequestBody Mood mood, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            Mood createdMood = moodService.createMood(mood, currentUser);
            return ResponseEntity.ok(createdMood);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/{moodId}")
    public ResponseEntity<?> updateMood(@PathVariable Integer moodId, @RequestBody Mood mood, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        mood.setMoodID(moodId);
        try {
            Mood updatedMood = moodService.updateMood(mood, currentUser);
            return ResponseEntity.ok(updatedMood);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{moodId}")
    public ResponseEntity<?> deleteMood(@PathVariable Integer moodId, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            moodService.deleteMood(moodId, currentUser);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserMoods(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        List<Mood> moods = moodService.getUserMoods(currentUser);
        return ResponseEntity.ok(moods);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentMood(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            Mood currentMood = moodService.getCurrentMood(currentUser);
            return ResponseEntity.ok(currentMood);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}