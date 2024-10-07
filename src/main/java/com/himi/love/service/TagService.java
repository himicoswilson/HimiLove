package com.himi.love.service;

import com.himi.love.model.Tag;
import com.himi.love.model.User;
import com.himi.love.model.Couple;
import java.util.List;

public interface TagService {
    Tag createTag(Tag tag, User currentUser, Couple couple);
    Tag updateTag(Tag tag, User currentUser, Couple couple);
    void deleteTag(Integer tagId, User currentUser, Couple couple);
    List<Tag> getAllTags(User currentUser, Couple couple);
}