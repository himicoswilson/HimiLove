package com.himi.love.mapper;

import com.himi.love.dto.EntityDTO;
import com.himi.love.model.Entity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EntityMapper {
    Entity findById(Integer entityId);
    List<Entity> findByCoupleId(Integer coupleId);
    List<Entity> findByType(String entityType);
    int insert(Entity entity);
    int update(Entity entity);
    int deleteById(Integer entityId);
    List<EntityDTO> findEntitiesWithUnviewedStatus(@Param("coupleId") Integer coupleId, @Param("userId") Integer userId);
}