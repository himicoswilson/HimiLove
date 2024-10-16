package com.himi.love.mapper;

import com.himi.love.dto.PostDTO;
import com.himi.love.model.PostEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PostEntityMapper {
    int insert(PostEntity postEntity);
    int deleteByPostIdAndEntityId(@Param("postId") Integer postId, @Param("entityId") Integer entityId);
    List<Integer> findEntityIdsByPostId(Integer postId);
    List<Integer> findPostIdsByEntityId(Integer entityId);
    List<PostDTO> findPostsByEntityIdWithPagination(@Param("entityId") Integer entityId, @Param("offset") int offset, @Param("limit") int limit);
    LocalDateTime findLatestPostTimeByEntityId(@Param("entityId") Integer entityId);
}