package com.himi.love.service.impl;

import com.himi.love.mapper.TagMapper;
import com.himi.love.model.Couple;
import com.himi.love.model.Tag;
import com.himi.love.model.User;
import com.himi.love.service.TagService;
import com.himi.love.service.UserService;
import com.himi.love.service.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CoupleService coupleService;

    @Override
    @CacheEvict(cacheNames = "tags", key = "#couple.getCoupleID()")
    public Tag createTag(Tag tag, User currentUser, Couple couple) {
        tag.setCreatorID(currentUser.getUserID());
        tagMapper.insert(tag);
        return tag;
    }

    @Override
    @CacheEvict(cacheNames = "tags", key = "#couple.getCoupleID()")
    public Tag updateTag(Tag tag, User currentUser, Couple couple) {
        Tag existingTag = getTagById(tag.getTagID(), currentUser, couple);
        existingTag.setTagName(tag.getTagName());
        tagMapper.update(existingTag);
        return existingTag;
    }

    @Override
    @CacheEvict(cacheNames = "tags", key = "#couple.getCoupleID()")
    public void deleteTag(Integer tagId, User currentUser, Couple couple) {
        getTagById(tagId, currentUser, couple);
        tagMapper.deleteById(tagId);
    }

    @Override
    @Cacheable(cacheNames = "tags", key = "#couple.getCoupleID()", unless = "#result == null")
    public List<Tag> getAllTags(User currentUser, Couple couple) {
        System.out.println("getAllTags");
        return tagMapper.findByCoupleId(couple.getCoupleID());
    }

    private Tag getTagById(Integer tagId, User currentUser, Couple couple) {
        Tag tag = tagMapper.findById(tagId);
        if (tag == null) {
            throw new RuntimeException("标签不存在");
        }

        // 获取标签创建者的 Couple ID
        User tagCreator = userService.getUserById(tag.getCreatorID());
        Couple tagCreatorCouple = coupleService.getCoupleByUser(tagCreator);

        // 比较两个 Couple ID
        if (tagCreatorCouple == null || couple == null || 
            !tagCreatorCouple.getCoupleID().equals(couple.getCoupleID())) {
            throw new RuntimeException("无权操作此标签");
        }

        return tag;
    }
}