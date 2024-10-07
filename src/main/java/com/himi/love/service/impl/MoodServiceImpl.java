package com.himi.love.service.impl;

import com.himi.love.mapper.MoodMapper;
import com.himi.love.model.Mood;
import com.himi.love.model.User;
import com.himi.love.service.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MoodServiceImpl implements MoodService {

    @Autowired
    private MoodMapper moodMapper;

    @Override
    @CacheEvict(cacheNames = {"moods", "currentMood"}, key = "#currentUser.getUserID()")
    public Mood createMood(Mood mood, User currentUser) {
        Mood activeMood = getCurrentMood(currentUser);
        if (activeMood != null) {
            activeMood.setEndTime(LocalDateTime.now());
            activeMood.setDeleted(true);
            moodMapper.update(activeMood);
        }
        
        mood.setUserID(currentUser.getUserID());
        mood.setStartTime(LocalDateTime.now());
        moodMapper.insert(mood);
        return mood;
    }

    @Override
    @CacheEvict(cacheNames = {"moods", "currentMood"}, key = "#currentUser.getUserID()")
    public Mood updateMood(Mood mood, User currentUser) {
        Mood existingMood = moodMapper.findById(mood.getMoodID());
        if (existingMood == null) {
            throw new RuntimeException("心情不存在");
        }
        try {
            checkMoodOwnership(existingMood, currentUser);
        } catch (RuntimeException e) {
            throw new RuntimeException("您没有权限更新此心情", e);
        }
        moodMapper.update(mood);
        return mood;
    }

    @Override
    @CacheEvict(cacheNames = {"moods", "currentMood"}, key = "#currentUser.getUserID()")
    public void deleteMood(Integer moodId, User currentUser) {
        Mood existingMood = moodMapper.findById(moodId);
        if (existingMood == null) {
            throw new RuntimeException("心情不存在");
        }
        try {
            checkMoodOwnership(existingMood, currentUser);
        } catch (RuntimeException e) {
            throw new RuntimeException("您没有权限删除此心情", e);
        }
        moodMapper.softDelete(moodId);
    }

    @Override
    @Cacheable(cacheNames = "moods", key = "#currentUser.getUserID()", unless = "#result == null")
    public List<Mood> getUserMoods(User currentUser) {
        return moodMapper.findByUserId(currentUser.getUserID());
    }

    @Override
    @Cacheable(cacheNames = "currentMood", key = "#currentUser.getUserID()", unless = "#result == null")
    public Mood getCurrentMood(User currentUser) {
        return moodMapper.findCurrentMoodByUserId(currentUser.getUserID());
    }

    private void checkMoodOwnership(Mood mood, User currentUser) {
        if (!mood.getUserID().equals(currentUser.getUserID())) {
            throw new RuntimeException("无权操作此心情");
        }
    }
}