package com.himi.love.service.impl;

import com.himi.love.dto.*;
import com.himi.love.model.Post;
import com.himi.love.model.User;
import com.himi.love.model.Couple;
import com.himi.love.service.PostService;
import com.himi.love.service.CoupleService;
import com.himi.love.mapper.PostMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private CoupleService coupleService;

    @Override
    @CacheEvict(cacheNames = "posts", key = "#couple.getCoupleID()")
    public Post createPost(Post post, User currentUser, Couple couple) {
        System.out.println("createPost");
        if (couple == null) {
            throw new RuntimeException("用户不属于任何情侣关系");
        }
        post.setUserID(currentUser.getUserID());
        post.setCoupleID(couple.getCoupleID());

        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        postMapper.insert(post);
        return post;
    }

    @Override
    @Cacheable(cacheNames = "posts", key = "#couple.getCoupleID() + ':' + #postId", unless = "#result == null")
    public PostDTO getPostById(Integer postId, User currentUser, Couple couple) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        PostDTO postDTO = convertToPostDTO(post);
        if (!isAllowedToAccessPost(postDTO, currentUser)) {
            throw new RuntimeException("没有权限访问帖子");
        }
        return postDTO;
    }

    @Override
    @Cacheable(cacheNames = "posts", key = "#couple.getCoupleID()", unless = "#result == null")
    public List<PostDTO> getAllPosts(User currentUser, Couple couple) {
        List<Post> posts = postMapper.findByCoupleId(couple.getCoupleID());
        List<PostDTO> postDTOs = new ArrayList<>();

        for (Post post : posts) {
            PostDTO postDTO = convertToPostDTO(post);
            postDTOs.add(postDTO);
        }

        return postDTOs;
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "posts", key = "#couple.getCoupleID() + ':' + #postId"),
        @CacheEvict(cacheNames = "posts", key = "#couple.getCoupleID()")
    })
    public Post updatePost(Integer postId, Post post, User currentUser, Couple couple) {
        System.out.println("updatePost");
        Post existingPost = postMapper.findById(postId);
        if (existingPost == null) {
            throw new RuntimeException("帖子不存在");
        }
        existingPost.setContent(post.getContent());
        LocalDateTime now = LocalDateTime.now();
        existingPost.setUpdatedAt(now);
        postMapper.update(existingPost);
        return existingPost;
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "posts", key = "#couple.getCoupleID() + ':' + #postId"),
        @CacheEvict(cacheNames = "posts", key = "#couple.getCoupleID()")
    })
    public void deletePost(Integer postId, User currentUser, Couple couple) {
        System.out.println("deletePost");
        Post existingPost = postMapper.findById(postId);
        if (existingPost == null) {
            throw new RuntimeException("帖子不存在");
        }
        postMapper.deleteById(postId);
    }

    @Override
    @Cacheable(cacheNames = "posts", key = "#locationId + ':' + #couple.getCoupleID()", unless = "#result == null")
    public List<Post> getPostsByLocationId(Integer locationId, User currentUser, Couple couple) {
        System.out.println("getPostsByLocationId");
        if (couple == null) {
            return List.of();
        }
        return postMapper.findByLocationId(locationId);
    }

    @Override
    public boolean isAllowedToAccessPost(PostDTO post, User currentUser) {
        Couple couple = coupleService.getCoupleByUser(currentUser);
        return couple != null && couple.getCoupleID().equals(post.getCoupleID());
    }

    @Override
    public boolean isPostOwner(Integer postId, User currentUser) {
        Post post = postMapper.findById(postId);
        return post != null && post.getUserID().equals(currentUser.getUserID());
    }

    private PostDTO convertToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        // 设置基本字段
        postDTO.setPostID(post.getPostID());
        postDTO.setContent(post.getContent());
        postDTO.setUserID(post.getUserID());
        postDTO.setCoupleID(post.getCoupleID());
        postDTO.setLocationID(post.getLocationID());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        postDTO.setDeleted(post.isIsDeleted());

        // 设置用户信息
        User user = postMapper.getUserByPostId(post.getPostID());
        postDTO.setUserName(user.getUserName());
        postDTO.setUserAvatar(user.getAvatar());

        // 设置图片列表
        List<ImageDTO> images = postMapper.findImagesByPostId(post.getPostID());
        postDTO.setImages(images);

        // 设置标签列表
        List<TagDTO> tags = postMapper.findTagsByPostId(post.getPostID());
        postDTO.setTags(tags);

        // 设置实体列表
        List<EntityDTO> entities = postMapper.findEntitiesByPostId(post.getPostID());
        postDTO.setEntities(entities);

        // 设置评论列表
        List<CommentDTO> comments = postMapper.findCommentsByPostId(post.getPostID());
        postDTO.setComments(comments);

        return postDTO;
    }
}