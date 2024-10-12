package com.himi.love.service;

import org.springframework.web.multipart.MultipartFile;
import com.himi.love.model.User;
import com.himi.love.model.Couple;
import com.himi.love.model.Entity;

public interface ImageUploadService {
    String uploadPostImage(MultipartFile file, User user, Couple couple);
    String uploadUserAvatar(MultipartFile file, User user);
    String uploadEntityAvatar(MultipartFile file, Entity entity);
    String uploadCoupleBg(MultipartFile file, Couple couple);
}
