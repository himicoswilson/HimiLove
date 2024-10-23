package com.himi.love.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CommentDTO implements Serializable {
    private Integer commentID;  // 评论的唯一标识符
    private String content;      // 评论内容
    private Integer userID;      // 评论用户的ID
    private String userName;     // 评论用户的名称
    private String nickName;     // 评论用户的昵称
    private String userAvatar;   // 评论用户的头像
    private Integer postID;      // 关联帖子的ID
    private Integer parentCommentID; // 父评论的ID（用于支持嵌套评论）
    private boolean isDeleted;   // 评论是否被删除
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间

    // Getters and Setters

    public Integer getCommentID() {
        return commentID;
    }

    public void setCommentID(Integer commentID) {
        this.commentID = commentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public Integer getParentCommentID() {
        return parentCommentID;
    }

    public void setParentCommentID(Integer parentCommentID) {
        this.parentCommentID = parentCommentID;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}