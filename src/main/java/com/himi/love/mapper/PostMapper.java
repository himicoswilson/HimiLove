package com.himi.love.mapper;

import com.himi.love.dto.*;
import com.himi.love.model.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PostMapper {
    Post findById(Integer postId);
    List<Post> findAll();
    List<Post> findByUserId(Integer userId);
    List<Post> findByCoupleId(Integer coupleId);
    List<Post> findByLocationId(Integer locationId);
    List<PostDTO> findNearbyPosts(@Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude, @Param("radiusInMeters") double radiusInMeters, @Param("offset") int offset, @Param("size") int size, @Param("coupleId") Integer coupleId);
    int insert(Post post);
    Integer getLastInsertId();
    int update(Post post);
    int deleteById(Integer postId);
    List<PostDTO> findPostDetailsByCoupleIdWithPagination(@Param("coupleId") Integer coupleId, @Param("offset") int offset, @Param("limit") int limit);
    PostDTO findPostDetailById(@Param("postId") Integer postId);
}
