package com.himi.love.service.impl;

import com.himi.love.model.Comment;
import com.himi.love.model.User;
import com.himi.love.dto.PostDTO;
import com.himi.love.service.CommentService;
import com.himi.love.service.PostService;
import com.himi.love.service.CoupleService;
import com.himi.love.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

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

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"comment", "commentsByPost"}, allEntries = true)
    public Comment createComment(Comment comment, User currentUser) {
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
        
        comment.setUserID(currentUser.getUserID());
        comment.setDeleted(false);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        
        commentMapper.insert(comment);
        return comment;
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