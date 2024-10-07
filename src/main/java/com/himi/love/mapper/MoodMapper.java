package com.himi.love.mapper;

import com.himi.love.model.Mood;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MoodMapper {
    Mood findById(Integer moodId);
    List<Mood> findByUserId(Integer userId);
    Mood findCurrentMoodByUserId(Integer userId);
    int insert(Mood mood);
    int update(Mood mood);
    int softDelete(Integer moodId);
}