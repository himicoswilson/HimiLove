package com.himi.love.mapper;

import com.himi.love.dto.*;
import com.himi.love.model.Post;
import com.himi.love.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper {
    Post findById(Integer postId);
    List<Post> findAll();
    List<Post> findByUserId(Integer userId);
    List<Post> findByCoupleId(Integer coupleId);
    List<Post> findByLocationId(Integer locationId);
    int insert(Post post);
    Integer getLastInsertId();
    int update(Post post);
    int deleteById(Integer postId);

    // 获取帖子关联的用户信息
    User getUserByPostId(@Param("postId") Integer postId);

    // 获取帖子关联的图片列表
    List<ImageDTO> findImagesByPostId(@Param("postId") Integer postId);

    // 获取帖子关联的标签
    List<TagDTO> findTagsByPostId(@Param("postId") Integer postId);

    // 获取帖子关联的实体
    List<EntityDTO> findEntitiesByPostId(@Param("postId") Integer postId);

    // 获取帖子的评论列表
    List<CommentDTO> findCommentsByPostId(@Param("postId") Integer postId);

    List<Post> findByCoupleIdWithPagination(@Param("coupleId") Integer coupleId, @Param("offset") int offset, @Param("limit") int limit);
}