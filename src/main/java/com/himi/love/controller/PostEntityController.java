package com.himi.love.controller;

import com.himi.love.service.PostEntityService;
import com.himi.love.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/entities")
public class PostEntityController {

    @Autowired
    private PostEntityService postEntityService;

    @Autowired
    private UserService userService;

    @PostMapping("/{entityId}")
    public ResponseEntity<Void> addEntityToPost(@PathVariable Integer postId, @PathVariable Integer entityId, @AuthenticationPrincipal UserDetails userDetails) {
        postEntityService.addEntityToPost(postId, entityId, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{entityId}")
    public ResponseEntity<Void> removeEntityFromPost(@PathVariable Integer postId, @PathVariable Integer entityId, @AuthenticationPrincipal UserDetails userDetails) {
        postEntityService.removeEntityFromPost(postId, entityId, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Integer>> getEntitiesForPost(@PathVariable Integer postId, @AuthenticationPrincipal UserDetails userDetails) {
        List<Integer> entityIds = postEntityService.getEntityIdsByPostId(postId, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.ok(entityIds);
    }
}