package com.himi.love.controller;

import com.himi.love.dto.EntityDTO;
import com.himi.love.model.Entity;
import com.himi.love.model.User;
import com.himi.love.service.EntityService;
import com.himi.love.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entities")
public class EntityController {

    @Autowired
    private EntityService entityService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Entity> createEntity(@RequestBody Entity entity, @AuthenticationPrincipal UserDetails userDetails) {
        Entity createdEntity = entityService.createEntity(entity, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.ok(createdEntity);
    }

    @GetMapping("/{entityId}")
    public ResponseEntity<Entity> getEntity(@PathVariable Integer entityId, @AuthenticationPrincipal UserDetails userDetails) {
        Entity entity = entityService.getEntityById(entityId, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/{entityId}")
    public ResponseEntity<Entity> updateEntity(@PathVariable Integer entityId, @RequestBody Entity entity, @AuthenticationPrincipal UserDetails userDetails) {
        entity.setEntityID(entityId);
        Entity updatedEntity = entityService.updateEntity(entity, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{entityId}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Integer entityId, @AuthenticationPrincipal UserDetails userDetails) {
        entityService.deleteEntity(entityId, userService.getUserByUsername(userDetails.getUsername()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EntityDTO>> getAllEntities(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        List<EntityDTO> entities = entityService.getAllEntitiesWithUnviewedStatus(currentUser);
        return ResponseEntity.ok(entities);
    }
}