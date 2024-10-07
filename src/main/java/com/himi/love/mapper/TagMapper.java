package com.himi.love.mapper;

import com.himi.love.model.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {
    Tag findById(@Param("tagId") Integer tagId);
    List<Tag> findAll();
    List<Tag> findByCreatorId(@Param("creatorId") Integer creatorId);
    List<Tag> findByCoupleId(@Param("coupleId") Integer coupleId);
    int insert(Tag tag);
    int update(Tag tag);
    int deleteById(@Param("tagId") Integer tagId);
}