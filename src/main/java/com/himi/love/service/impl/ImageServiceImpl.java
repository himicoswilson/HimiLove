package com.himi.love.service.impl;

import com.himi.love.mapper.ImageMapper;
import com.himi.love.model.Image;
import com.himi.love.model.User;
import com.himi.love.model.Couple;
import com.himi.love.dto.PostDTO;
import com.himi.love.service.ImageService;
import com.himi.love.service.PostService;
import com.himi.love.service.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.ArrayList;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private CoupleService coupleService;

    @Cacheable(cacheNames = "image", key = "#imageId", unless = "#result == null")
    @Override
    public Image getImageById(Integer imageId, User currentUser) {
        Image image = imageMapper.findById(imageId);
        if (image == null) {
            throw new RuntimeException("图片不存在");
        }
        try {
            postService.getPostById(image.getPostID(), currentUser, coupleService.getCoupleByUser(currentUser));
        } catch (RuntimeException e) {
            throw new RuntimeException("您没有权限访问此图片", e);
        }
        return image;
    }

    @Cacheable(cacheNames = "images", key = "#currentUser.getUserID()", unless = "#result.isEmpty()")
    @Override
    public List<Image> getAllImages(User currentUser) {
        Couple couple = coupleService.getCoupleByUser(currentUser);
        if (couple == null) {
            return new ArrayList<>(); // 如果用户不在情侣关系中，返回空列表
        }
        return imageMapper.findByCoupleId(couple.getCoupleID());
    }

    @Override
    public List<Image> getImagesByPostId(Integer postId, User currentUser) {
        try {
            postService.getPostById(postId, currentUser, coupleService.getCoupleByUser(currentUser));
        } catch (RuntimeException e) {
            throw new RuntimeException("您没有权限访问此帖子的图片", e);
        }
        return imageMapper.findByPostId(postId);
    }

    @CacheEvict(cacheNames = {"image", "images"}, key = "#currentUser.getUserID()")
    @Transactional
    @Override
    public Image createImage(Image image, User currentUser) {
        PostDTO post = postService.getPostById(image.getPostID(), currentUser, coupleService.getCoupleByUser(currentUser));
        if (!post.getUserID().equals(currentUser.getUserID())) {
            throw new RuntimeException("您没有权限为此帖子添加图片");
        }
        imageMapper.insert(image);
        return image;
    }

    @CacheEvict(cacheNames = {"image", "images"}, key = "#currentUser.getUserID()")
    @Transactional
    @Override
    public Image updateImage(Image image, User currentUser) {
        Image existingImage = getImageById(image.getImageID(), currentUser);
        if (existingImage == null) {
            throw new RuntimeException("图片不存在");
        }
        PostDTO post = postService.getPostById(existingImage.getPostID(), currentUser, coupleService.getCoupleByUser(currentUser));
        if (!post.getUserID().equals(currentUser.getUserID())) {
            throw new RuntimeException("您没有权限修改此图片");
        }
        imageMapper.update(image);
        return image;
    }

    @CacheEvict(cacheNames = {"image", "images"}, key = "#currentUser.getUserID()")
    @Transactional
    @Override
    public void deleteImage(Integer imageId, User currentUser) {
        Image existingImage = getImageById(imageId, currentUser);
        if (existingImage == null) {
            throw new RuntimeException("图片不存在");
        }
        PostDTO post = postService.getPostById(existingImage.getPostID(), currentUser, coupleService.getCoupleByUser(currentUser));
        if (!post.getUserID().equals(currentUser.getUserID())) {
            throw new RuntimeException("您没有权限删除此图片");
        }
        imageMapper.deleteById(imageId);
    }
}