package com.himi.love.service;

import com.himi.love.dto.PostDTO;
import com.himi.love.model.Post;
import com.himi.love.model.User;
import com.himi.love.model.Couple;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Post createPost(String content, String tagsJson, String entitiesJson, MultipartFile[] images, User currentUser, Couple couple);
    PostDTO getPostById(Integer postId, User currentUser, Couple couple);
    List<PostDTO> getAllPosts(User currentUser, Couple couple, int page, int limit);
    Post updatePost(Integer postId, Post post, User currentUser, Couple couple);
    void deletePost(Integer postId, User currentUser, Couple couple);
    List<Post> getPostsByLocationId(Integer locationId, User currentUser, Couple couple);
    boolean isAllowedToAccessPost(PostDTO post, User currentUser);
    boolean isPostOwner(Integer postId, User currentUser);
}
