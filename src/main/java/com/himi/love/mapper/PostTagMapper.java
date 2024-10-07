package com.himi.love.mapper;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PostTagMapper {
    void insertPostTag(Integer postId, Integer tagId);
    void deletePostTag(Integer postId, Integer tagId);
    List<Integer> findTagIdsByPostId(Integer postId);
    List<Integer> findPostIdsByTagId(Integer tagId);
}