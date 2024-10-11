package com.himi.love.service;

import com.himi.love.dto.ImageDTO;
import com.himi.love.model.Image;
import com.himi.love.model.User;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image getImageById(Integer imageId, User currentUser);
    List<Image> getAllImages(User currentUser);
    List<Image> getImagesByPostId(Integer postId, User currentUser);
    ImageDTO createImage(MultipartFile file, Integer postID, Integer orderIndex, User currentUser);
    ImageDTO updateImage(ImageDTO image, User currentUser);
    void deleteImage(Integer imageId, User currentUser);
}