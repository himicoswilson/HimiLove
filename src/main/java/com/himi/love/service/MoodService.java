package com.himi.love.service;

import com.himi.love.model.Mood;
import com.himi.love.model.User;

import java.util.List;

public interface MoodService {
    Mood createMood(Mood mood, User currentUser);
    Mood updateMood(Mood mood, User currentUser);
    void deleteMood(Integer moodId, User currentUser);
    List<Mood> getUserMoods(User currentUser);
    Mood getCurrentMood(User currentUser);
}