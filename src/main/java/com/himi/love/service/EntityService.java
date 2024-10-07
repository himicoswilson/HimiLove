package com.himi.love.service;

import com.himi.love.dto.EntityDTO;
import com.himi.love.model.Entity;
import com.himi.love.model.User;

import java.util.List;

public interface EntityService {
    Entity createEntity(Entity entity, User currentUser);
    Entity getEntityById(Integer entityId, User currentUser);
    // List<Entity> getAllEntities(User currentUser);
    Entity updateEntity(Entity entity, User currentUser);
    void deleteEntity(Integer entityId, User currentUser);
    boolean isEntityAccessibleByUser(Entity entity, User currentUser);
    List<EntityDTO> getAllEntitiesWithUnviewedStatus(User currentUser);
}
