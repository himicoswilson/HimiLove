package com.himi.love.service.impl;

import com.himi.love.service.ImageUploadService;
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
    public String uploadImage(MultipartFile file) {
        String fileUrl = null;
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            ossClient.putObject(bucketName, fileName, inputStream);
            fileUrl = "https://" + bucketName + "." + endpoint + "/" + fileName; // 构建文件的访问 URL
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return fileUrl;
    }
}