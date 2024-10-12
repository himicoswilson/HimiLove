package com.himi.love.service.impl;

import com.himi.love.service.ImageUploadService;
import com.himi.love.model.User;
import com.himi.love.model.Couple;
import com.himi.love.model.Entity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Override
    public String uploadPostImage(MultipartFile file, User user, Couple couple) {
        return uploadFile(file, "post_images/" + couple.getCoupleID() + "/" + user.getUserID());
    }

    @Override
    public String uploadUserAvatar(MultipartFile file, User user) {
        return uploadFile(file, "user_avatar/" + user.getUserID());
    }

    @Override
    public String uploadEntityAvatar(MultipartFile file, Entity entity) {
        return uploadFile(file, "entity_avatar/" + entity.getEntityID());
    }

    @Override
    public String uploadCoupleBg(MultipartFile file, Couple couple) {
        return uploadFile(file, "couple_bg/" + couple.getCoupleID());
    }

    private String uploadFile(MultipartFile file, String path) {
        String fileUrl = null;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String objectName = path + "/" + fileName;
            ossClient.putObject(bucketName, objectName, inputStream);
            fileUrl = "https://" + bucketName + "." + endpoint + "/" + objectName;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return fileUrl;
    }
}
