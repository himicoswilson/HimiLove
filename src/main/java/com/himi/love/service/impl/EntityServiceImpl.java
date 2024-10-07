package com.himi.love.service.impl;

import com.himi.love.dto.EntityDTO;
import com.himi.love.mapper.EntityMapper;
import com.himi.love.model.Entity;
import com.himi.love.model.User;
import com.himi.love.service.EntityService;
import com.himi.love.service.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EntityServiceImpl implements EntityService {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private CoupleService coupleService;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"entity", "entitiesWithStatus"}, allEntries = true)
    public Entity createEntity(Entity entity, User currentUser) {
        Integer coupleId = coupleService.getCoupleByUser(currentUser).getCoupleID();
        entity.setCoupleID(coupleId);
        entityMapper.insert(entity);
        return entity;
    }

    // @Override
    // public List<Entity> getAllEntities(User currentUser) {
    //     Integer coupleId = coupleService.getCoupleByUserId(currentUser.getUserID()).getCoupleID();
    //     return entityMapper.findByCoupleId(coupleId);
    // }

    @Override
    @Cacheable(cacheNames = "entity", key = "#entityId", unless = "#result == null")
    public Entity getEntityById(Integer entityId, User currentUser) {
        Entity entity = entityMapper.findById(entityId);
        if (entity == null || !isEntityAccessibleByUser(entity, currentUser)) {
            throw new RuntimeException("实体不存在或无权访问");
        }
        return entity;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"entity", "entitiesWithStatus"}, allEntries = true)
    public Entity updateEntity(Entity entity, User currentUser) {
        Entity existingEntity = getEntityById(entity.getEntityID(), currentUser);
        existingEntity.setEntityName(entity.getEntityName());
        existingEntity.setAvatar(entity.getAvatar());
        entityMapper.update(existingEntity);
        return existingEntity;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"entity", "entitiesWithStatus"}, allEntries = true)
    public void deleteEntity(Integer entityId, User currentUser) {
        Entity entity = getEntityById(entityId, currentUser);
        if (entity == null || !isEntityAccessibleByUser(entity, currentUser)) {
            throw new RuntimeException("实体不存在或无权访问");
        }
        entityMapper.deleteById(entityId);
    }

    @Override
    @Cacheable(cacheNames = "entitiesWithStatus", key = "#currentUser.getUserID()", unless = "#result.isEmpty()")
    public List<EntityDTO> getAllEntitiesWithUnviewedStatus(User currentUser) {
        Integer coupleId = coupleService.getCoupleByUser(currentUser).getCoupleID();
        return entityMapper.findEntitiesWithUnviewedStatus(coupleId, currentUser.getUserID());
    }

    @Override
    public boolean isEntityAccessibleByUser(Entity entity, User currentUser) {
        return coupleService.getCoupleByUser(currentUser).getCoupleID().equals(entity.getCoupleID());
    }
}