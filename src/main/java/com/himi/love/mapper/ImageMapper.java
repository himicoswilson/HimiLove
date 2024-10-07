package com.himi.love.mapper;

import com.himi.love.model.Image;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {
    Image findById(Integer imageId);
    List<Image> findAll();
    List<Image> findByPostId(Integer postId);
    List<Image> findByCoupleId(Integer coupleId);
    int insert(Image image);
    int update(Image image);
    int deleteById(Integer imageId);
}