package com.himi.love.controller;

import com.himi.love.model.Tag;
import com.himi.love.service.PostTagService;
import com.himi.love.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/tags")
public class PostTagController {

    @Autowired
    private PostTagService postTagService;

    @Autowired
    private UserService userService;

    @PostMapping("/{tagId}")
    public ResponseEntity<Void> addTagToPost(@PathVariable Integer postId, @PathVariable Integer tagId, @AuthenticationPrincipal UserDetails userDetails) {
        postTagService.addTagToPost(postId, tagId, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> removeTagFromPost(@PathVariable Integer postId, @PathVariable Integer tagId, @AuthenticationPrincipal UserDetails userDetails) {
        postTagService.removeTagFromPost(postId, tagId, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getTagsForPost(@PathVariable Integer postId, @AuthenticationPrincipal UserDetails userDetails) {
        List<Tag> tags = postTagService.getTagsByPostId(postId, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.ok(tags);
    }
}