package com.himi.love.dto;

import java.io.Serializable;
import org.springframework.web.multipart.MultipartFile;

public class ImageDTO implements Serializable {
    private Integer imageID; // 图片的唯一标识符
    private String imageURL; // 图片的URL
    private Integer postID; // 关联的帖子的ID
    private Integer orderIndex; // 添加 OrderIndex 字段
    private MultipartFile file; // 添加文件字段

    // Getters and Setters
    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    // Getter 和 Setter 方法
    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
