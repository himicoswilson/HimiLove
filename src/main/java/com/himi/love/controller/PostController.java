package com.himi.love.controller;

import com.himi.love.dto.PostDTO;
import com.himi.love.model.Post;
import com.himi.love.model.User;
import com.himi.love.service.UserService;
import com.himi.love.service.CoupleService;
import com.himi.love.service.PostService;
import com.himi.love.service.PostEntityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private PostEntityService postEntityService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Post> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "tags", required = false) String tagsJson,
            @RequestParam(value = "entities", required = false) String entitiesJson,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        Post createdPost = postService.createPost(content, tagsJson, entitiesJson, images, currentUser, coupleService.getCoupleByUser(currentUser));
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Integer postId, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        PostDTO post = postService.getPostById(postId, currentUser, coupleService.getCoupleByUser(currentUser));
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        List<PostDTO> posts = postService.getAllPosts(currentUser, coupleService.getCoupleByUser(currentUser), page, limit);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Integer postId, @RequestBody Post post, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            Post updatedPost = postService.updatePost(postId, post, currentUser, coupleService.getCoupleByUser(currentUser));
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Integer postId, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        try {
            postService.deletePost(postId, currentUser, coupleService.getCoupleByUser(currentUser));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<?> getPostsByLocationId(@PathVariable Integer locationId, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        List<Post> posts = postService.getPostsByLocationId(locationId, currentUser, coupleService.getCoupleByUser(currentUser));
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/entity/{entityId}/{page}/{limit}")
    public ResponseEntity<?> getPostsByEntityId(@PathVariable Integer entityId, @PathVariable int page, @PathVariable int limit, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        List<PostDTO> posts = postEntityService.getPostsByEntityId(entityId, currentUser, page, limit);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/entity/{entityId}/has-unviewed")
    public ResponseEntity<?> hasUnviewedPosts(@PathVariable Integer entityId, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        boolean hasUnviewed = postEntityService.hasUnviewedPosts(entityId, currentUser);
        return ResponseEntity.ok(hasUnviewed);
    }
}
