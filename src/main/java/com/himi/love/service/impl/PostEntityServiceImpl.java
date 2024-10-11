package com.himi.love.service.impl;

import com.himi.love.dto.PostDTO;
import com.himi.love.mapper.PostEntityMapper;
import com.himi.love.mapper.UserEntityLastViewedMapper;
import com.himi.love.model.*;
import com.himi.love.service.PostEntityService;
import com.himi.love.service.PostService;
import com.himi.love.service.EntityService;
import com.himi.love.service.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostEntityServiceImpl implements PostEntityService {

    @Autowired
    private PostEntityMapper postEntityMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private UserEntityLastViewedMapper userEntityLastViewedMapper;

    @Override
    @Transactional
    public void addEntityToPost(Integer postID, Integer entityID, User currentUser) {
        PostDTO post = postService.getPostById(postID, currentUser, coupleService.getCoupleByUser(currentUser));
        Entity entity = entityService.getEntityById(entityID, currentUser);
        
        if (!isEntityAllowedForPost(entity, post, currentUser)) {
            throw new RuntimeException("无权为此帖子添加该实体");
        }
        
        PostEntity postEntity = new PostEntity(postID, entityID);
        postEntityMapper.insert(postEntity);
    }

    @Override
    @Transactional
    public void removeEntityFromPost(Integer postID, Integer entityID, User currentUser) {
        PostDTO post = postService.getPostById(postID, currentUser, coupleService.getCoupleByUser(currentUser));
        Entity entity = entityService.getEntityById(entityID, currentUser);
        
        if (!isEntityAllowedForPost(entity, post, currentUser)) {
            throw new RuntimeException("无权从此帖子移除该实体");
        }
        
        postEntityMapper.deleteByPostIdAndEntityId(postID, entityID);
    }

    @Override
    public List<Integer> getEntityIdsByPostId(Integer postID, User currentUser) {
        return postEntityMapper.findEntityIdsByPostId(postID);
    }

    @Override
    public List<Integer> getPostIdsByEntityId(Integer entityID, User currentUser) {
        return postEntityMapper.findPostIdsByEntityId(entityID);
    }

    @Override
    public List<PostDTO> getPostsByEntityId(Integer entityId, User currentUser) {
        List<PostDTO> posts = postEntityMapper.findPostsByEntityId(entityId);
        
        // 更新最后查看时间
        UserEntityLastViewed lastViewed = new UserEntityLastViewed();
        lastViewed.setUserID(currentUser.getUserID());
        lastViewed.setEntityID(entityId);
        lastViewed.setLastViewedTime(LocalDateTime.now());
        userEntityLastViewedMapper.insertOrUpdate(lastViewed);
        
        return posts;
    }

    @Override
    public boolean hasUnviewedPosts(Integer entityId, User currentUser) {
        UserEntityLastViewed lastViewed = userEntityLastViewedMapper.findByUserIdAndEntityId(currentUser.getUserID(), entityId);
        if (lastViewed == null) {
            return true; // 如果没有查看记录,则认为有未查看的帖子
        }
        
        LocalDateTime lastPostTime = postEntityMapper.findLatestPostTimeByEntityId(entityId);
        return lastPostTime != null && lastPostTime.isAfter(lastViewed.getLastViewedTime());
    }

    private boolean isEntityAllowedForPost(Entity entity, PostDTO post, User currentUser) {
        Couple couple = coupleService.getCoupleByUser(currentUser);
        return couple != null && couple.getCoupleID().equals(post.getCoupleID()) && couple.getCoupleID().equals(entity.getCoupleID());
    }
}