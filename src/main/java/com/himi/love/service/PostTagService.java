package com.himi.love.service;

import com.himi.love.model.User;
import com.himi.love.model.Tag;

import java.util.List;

public interface PostTagService {
    void addTagToPost(Integer postId, Integer tagId, User currentUser);
    void removeTagFromPost(Integer postId, Integer tagId, User currentUser);
    List<Tag> getTagsByPostId(Integer postId, User currentUser);
}