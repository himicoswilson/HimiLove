package com.himi.love.service;

import com.himi.love.dto.PostDTO;
import com.himi.love.model.User;

import java.util.List;

public interface PostEntityService {
    void addEntityToPost(Integer postID, Integer entityID, User currentUser);
    void removeEntityFromPost(Integer postID, Integer entityID, User currentUser);
    List<Integer> getEntityIdsByPostId(Integer postID, User currentUser);
    List<Integer> getPostIdsByEntityId(Integer entityID, User currentUser);
    List<PostDTO> getPostsByEntityId(Integer entityId, User currentUser);
    boolean hasUnviewedPosts(Integer entityId, User currentUser);
}