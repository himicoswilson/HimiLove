package com.himi.love.controller;

import com.himi.love.model.Tag;
import com.himi.love.model.User;
import com.himi.love.service.TagService;
import com.himi.love.service.UserService;
import com.himi.love.service.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoupleService coupleService;

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            Tag createdTag = tagService.createTag(tag, currentUser, coupleService.getCoupleByUser(currentUser));
            return ResponseEntity.ok(createdTag);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<?> updateTag(@PathVariable Integer tagId, @RequestBody Tag tag, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        tag.setTagID(tagId);
        try {
            Tag updatedTag = tagService.updateTag(tag, currentUser, coupleService.getCoupleByUser(currentUser));
            return ResponseEntity.ok(updatedTag);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable Integer tagId, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            tagService.deleteTag(tagId, currentUser, coupleService.getCoupleByUser(currentUser));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTags(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        List<Tag> tags = tagService.getAllTags(currentUser, coupleService.getCoupleByUser(currentUser));
        return ResponseEntity.ok(tags);
    }
}