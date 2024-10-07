package com.himi.love.service.impl;

import com.himi.love.mapper.PostTagMapper;
import com.himi.love.mapper.TagMapper;
import com.himi.love.model.Tag;
import com.himi.love.model.User;
import com.himi.love.service.PostTagService;
import com.himi.love.service.PostService;
import com.himi.love.service.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostTagServiceImpl implements PostTagService {

    @Autowired
    private PostTagMapper postTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private PostService postService;

    @Autowired
    private CoupleService coupleService;

    @Override
    @Transactional
    public void addTagToPost(Integer postId, Integer tagId, User currentUser) {
        if (!postService.isPostOwner(postId, currentUser)) {
            throw new RuntimeException("无权为此帖子添加标签");
        }
        postTagMapper.insertPostTag(postId, tagId);
    }

    @Override
    @Transactional
    public void removeTagFromPost(Integer postId, Integer tagId, User currentUser) {
        if (!postService.isPostOwner(postId, currentUser)) {
            throw new RuntimeException("无权从此帖子移除标签");
        }
        postTagMapper.deletePostTag(postId, tagId);
    }

    @Override
    public List<Tag> getTagsByPostId(Integer postId, User currentUser) {
        postService.getPostById(postId, currentUser, coupleService.getCoupleByUser(currentUser));
        List<Integer> tagIds = postTagMapper.findTagIdsByPostId(postId);
        return tagIds.stream().map(tagMapper::findById).collect(Collectors.toList());
    }
}