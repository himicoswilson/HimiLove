package com.himi.love.service;

import com.himi.love.dto.CommentDTO;
import com.himi.love.model.Comment;
import com.himi.love.model.User;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(Comment comment, User currentUser);
    Comment getCommentById(Integer commentId, User currentUser);
    List<Comment> getCommentsByPostId(Integer postId, User currentUser);
    Comment updateComment(Integer commentId, Comment comment, User currentUser);
    void deleteComment(Integer commentId, User currentUser);
}