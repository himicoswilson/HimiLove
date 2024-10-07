package com.himi.love.service;

import com.himi.love.model.Image;
import com.himi.love.model.User;

import java.util.List;

public interface ImageService {
    Image getImageById(Integer imageId, User currentUser);
    List<Image> getAllImages(User currentUser);
    List<Image> getImagesByPostId(Integer postId, User currentUser);
    Image createImage(Image image, User currentUser);
    Image updateImage(Image image, User currentUser);
    void deleteImage(Integer imageId, User currentUser);
}