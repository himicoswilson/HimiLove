package com.himi.love.service.impl;

import com.himi.love.model.Comment;
import com.himi.love.model.Couple;
import com.himi.love.model.User;
import com.himi.love.dto.CommentDTO;
import com.himi.love.dto.PostDTO;
import com.himi.love.service.CommentService;
import com.himi.love.service.PostService;
import com.himi.love.service.CoupleService;
import com.himi.love.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"comment", "commentsByPost"}, allEntries = true)
    public CommentDTO createComment(Comment comment, User currentUser) {
        PostDTO post = postService.getPostById(comment.getPostID(), currentUser, coupleService.getCoupleByUser(currentUser));
        if (!isAllowedToComment(post, currentUser)) {
            throw new RuntimeException("您没有权限对此帖子进行评论");
        }
        
        if (comment.getParentCommentID() != null) {
            Comment parentComment = commentMapper.findById(comment.getParentCommentID());
            if (parentComment == null || !parentComment.getPostID().equals(comment.getPostID())) {
                throw new RuntimeException("父评论不存在或不属于此帖子");
            }
        }

        CommentDTO commentDto = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDto);
        
        commentDto.setUserID(currentUser.getUserID());
        commentDto.setNickName(currentUser.getNickName());
        commentDto.setUserName(currentUser.getUserName());
        commentDto.setUserAvatar(currentUser.getAvatar());
        commentDto.setDeleted(false);
        commentDto.setCreatedAt(LocalDateTime.now());
        commentDto.setUpdatedAt(LocalDateTime.now());

        // 清除帖子缓存
        Couple couple = coupleService.getCoupleByUser(currentUser);
        if (couple != null) {
            // 增加缓存版本号
            String versionKey = "posts:version:" + couple.getCoupleID();
            redisTemplate.opsForValue().increment(versionKey);

            // 只清除第一页缓存
            String firstPageCacheKey = couple.getCoupleID() + ":posts:1:10:*";
            redisTemplate.delete(firstPageCacheKey);
        }
        
        commentMapper.insert(commentDto);
        return commentDto;
    }

    @Override
    @Cacheable(cacheNames = "comment", key = "#commentId", unless = "#result == null")
    public Comment getCommentById(Integer commentId, User currentUser) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null || comment.isDeleted()) {
            throw new RuntimeException("评论不存在");
        }
        postService.getPostById(comment.getPostID(), currentUser, coupleService.getCoupleByUser(currentUser));
        return comment;
    }

    @Override
    @Cacheable(cacheNames = "commentsByPost", key = "#postId", unless = "#result.isEmpty()")
    public List<Comment> getCommentsByPostId(Integer postId, User currentUser) {
        postService.getPostById(postId, currentUser, coupleService.getCoupleByUser(currentUser));
        return commentMapper.findByPostId(postId);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"comment", "commentsByPost"}, allEntries = true)
    public Comment updateComment(Integer commentId, Comment comment, User currentUser) {
        Comment existingComment = getCommentById(commentId, currentUser);
        if (!existingComment.getUserID().equals(currentUser.getUserID())) {
            throw new RuntimeException("您没有权限修改此评论");
        }
        existingComment.setContent(comment.getContent());
        existingComment.setUpdatedAt(LocalDateTime.now());
        commentMapper.update(existingComment);
        return existingComment;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"comment", "commentsByPost"}, allEntries = true)
    public void deleteComment(Integer commentId, User currentUser) {
        Comment existingComment = getCommentById(commentId, currentUser);
        if (!existingComment.getUserID().equals(currentUser.getUserID())) {
            throw new RuntimeException("您没有权限删除此评论");
        }
        existingComment.setDeleted(true);
        existingComment.setUpdatedAt(LocalDateTime.now());
        commentMapper.update(existingComment);
    }

    private boolean isAllowedToComment(PostDTO post, User currentUser) {
        return post.getUserID().equals(currentUser.getUserID()) || 
            postService.isAllowedToAccessPost(post, currentUser);
    }
}