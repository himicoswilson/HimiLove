package com.himi.love.mapper;

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
    int insert(Comment comment);
    int update(Comment comment);
}