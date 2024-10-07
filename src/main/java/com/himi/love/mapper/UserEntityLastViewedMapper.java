package com.himi.love.mapper;

import com.himi.love.model.UserEntityLastViewed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserEntityLastViewedMapper {
    UserEntityLastViewed findByUserIdAndEntityId(@Param("userId") Integer userId, @Param("entityId") Integer entityId);
    void insertOrUpdate(UserEntityLastViewed userEntityLastViewed);
}