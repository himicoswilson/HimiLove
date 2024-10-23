package com.himi.love.mapper;

import com.himi.love.dto.CommentDTO;
import com.himi.love.model.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    Comment findById(Integer commentId);
    List<Comment> findAll();
    List<Comment> findByPostId(Integer postId);
    List<Comment> findByUserId(Integer userId);
    List<Comment> findByParentCommentId(Integer parentCommentId);
    int insert(CommentDTO comment);
    int update(Comment comment);
}