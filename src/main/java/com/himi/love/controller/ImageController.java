package com.himi.love.controller;

import com.himi.love.model.Image;
import com.himi.love.model.User;
import com.himi.love.service.ImageService;
import com.himi.love.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllImages(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        List<Image> images = imageService.getAllImages(currentUser);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImageById(@PathVariable Integer imageId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            Image image = imageService.getImageById(imageId, currentUser);
            return ResponseEntity.ok(image);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getImagesByPostId(@PathVariable Integer postId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            List<Image> images = imageService.getImagesByPostId(postId, currentUser);
            return ResponseEntity.ok(images);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createImage(@RequestBody Image image, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            Image createdImage = imageService.createImage(image, currentUser);
            return ResponseEntity.ok(createdImage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<?> updateImage(@PathVariable Integer imageId, @RequestBody Image image, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        image.setImageID(imageId);
        try {
            Image updatedImage = imageService.updateImage(image, currentUser);
            return ResponseEntity.ok(updatedImage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Integer imageId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
        }
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            imageService.deleteImage(imageId, currentUser);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}