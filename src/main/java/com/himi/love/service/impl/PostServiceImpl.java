package com.himi.love.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.himi.love.dto.*;
import com.himi.love.model.Post;
import com.himi.love.model.User;
import com.himi.love.model.Couple;
import com.himi.love.service.PostService;
import com.himi.love.service.CoupleService;
import com.himi.love.mapper.PostMapper;
import com.himi.love.mapper.ImageMapper;
import com.himi.love.mapper.PostTagMapper;
import com.himi.love.service.ImageUploadService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import com.himi.love.service.UserService;
import com.himi.love.service.PostEntityService;

import org.springframework.context.annotation.Lazy;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private PostTagMapper postTagMapper;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    @Lazy
    private PostEntityService postEntityService;

    @Override
    @Transactional
    public Post createPost(String content, String tagsJson, String entitiesJson, MultipartFile[] images, User currentUser, Couple couple) {
        // 创建帖子
        Post postEntity = new Post();
        postEntity.setContent(content);
        postEntity.setUserID(currentUser.getUserID());
        postEntity.setCoupleID(couple.getCoupleID());
        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        postEntity.setCreatedAt(now);
        postEntity.setUpdatedAt(now);
        postMapper.insert(postEntity);
        // 获取最后插入的ID
        Integer lastInsertId = postMapper.getLastInsertId();
        postEntity.setPostID(lastInsertId);
        
        // 处理图片
        if (images != null) {
            for (int i = 0; i < images.length; i++) {
                MultipartFile imageFile = images[i];
                String imageUrl = imageUploadService.uploadPostImage(imageFile, currentUser, couple); // 上传图片
                
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setImageURL(imageUrl);
                imageDTO.setPostID(postEntity.getPostID());
                imageDTO.setOrderIndex(i); // 设置顺序索引
                
                imageMapper.insert(imageDTO); // 插入图片
            }
        }

        // 处理标签
        if (tagsJson != null) {
            // 使用 ObjectMapper 将 JSON 字符串转换为 TagDTO 列表
            ObjectMapper objectMapper = new ObjectMapper();
            List<TagDTO> tags;
            try {
                tags = objectMapper.readValue(tagsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, TagDTO.class));
            } catch (JsonProcessingException e) {
                // Handle the exception (e.g., log it or rethrow it)
                e.printStackTrace(); // 或者使用日志记录
                return null; // 或者根据需要处理
            }

            for (TagDTO tag : tags) {
                // 插入标签到数据库
                postTagMapper.insertPostTag(postEntity.getPostID(), tag.getTagID());
            }
        }

        // 处理实体
        if (entitiesJson != null) {
            // 使用 ObjectMapper 将 JSON 字符串转换为 EntityDTO 列表
            ObjectMapper objectMapper = new ObjectMapper();
            List<EntityDTO> entities;
            try {
                // 修改为使用 TypeReference 进行反序列化
                entities = objectMapper.readValue(entitiesJson, new TypeReference<List<EntityDTO>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // 或者使用日志记录
                return null; // 或者根据需要处理
            }
            for (EntityDTO entity : entities) {
                // 插入实体到数据库
                postEntityService.addEntityToPost(postEntity.getPostID(), entity.getEntityID(), currentUser);
            }
        }

        // 增加缓存版本号
        String versionKey = "posts:version:" + couple.getCoupleID();
        redisTemplate.opsForValue().increment(versionKey);

        // 只清除第一页缓存
        String firstPageCacheKey = couple.getCoupleID() + ":posts:1:10:*";
        redisTemplate.delete(firstPageCacheKey);

        // 清除 entitiesWithStatus 缓存
        List<String> keysToDelete = List.of(
            "entitiesWithStatus::" + couple.getUserID1(),
            "entitiesWithStatus::" + couple.getUserID2()
        );
        redisTemplate.delete(keysToDelete);

        return postEntity;
    }

    @Override
    @Transactional
    @Cacheable(cacheNames = "posts", key = "#couple.getCoupleID() + ':' + #postId", unless = "#result == null")
    public PostDTO getPostById(Integer postId, User currentUser, Couple couple) {
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }
        PostDTO postDTO = convertToPostDTO(post);
        if (!isAllowedToAccessPost(postDTO, currentUser)) {
            throw new RuntimeException("没有权限访问帖子");
        }
        return postDTO;
    }

    @Override
    @Transactional
    @Cacheable(cacheNames = "posts", key = "#couple.getCoupleID() + ':posts:' + #page + ':' + #limit + ':' + @redisTemplate.opsForValue().get('posts:version:' + #couple.getCoupleID())", unless = "#result == null")
    public List<PostDTO> getAllPosts(User currentUser, Couple couple, int page, int limit) {
        int offset = (page - 1) * limit;
        List<Post> posts = postMapper.findByCoupleIdWithPagination(couple.getCoupleID(), offset, limit);
        List<PostDTO> postDTOs = new ArrayList<>();

        for (Post post : posts) {
            PostDTO postDTO = convertToPostDTO(post);
            postDTOs.add(postDTO);
        }

        return postDTOs;
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "posts", key = "#couple.getCoupleID() + ':' + #postId"),
        @CacheEvict(cacheNames = "posts", key = "#couple.getCoupleID() + ':' + #page + ':' + #limit")
    })
    public Post updatePost(Integer postId, Post post, User currentUser, Couple couple) {
        System.out.println("updatePost");
        Post existingPost = postMapper.findById(postId);
        if (existingPost == null) {
            throw new RuntimeException("帖子不存在");
        }
        existingPost.setContent(post.getContent());
        LocalDateTime now = LocalDateTime.now();
        existingPost.setUpdatedAt(now);
        postMapper.update(existingPost);
        return existingPost;
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "posts", key = "#couple.getCoupleID() + ':' + #postId"),
        @CacheEvict(cacheNames = "posts", key = "#couple.getCoupleID() + ':' + #page + ':' + #limit")
    })
    public void deletePost(Integer postId, User currentUser, Couple couple) {
        System.out.println("deletePost");
        Post existingPost = postMapper.findById(postId);
        if (existingPost == null) {
            throw new RuntimeException("帖子不存在");
        }
        postMapper.deleteById(postId);
    }

    @Override
    @Cacheable(cacheNames = "posts", key = "#locationId + ':' + #couple.getCoupleID()", unless = "#result == null")
    public List<Post> getPostsByLocationId(Integer locationId, User currentUser, Couple couple) {
        System.out.println("getPostsByLocationId");
        if (couple == null) {
            return List.of();
        }
        return postMapper.findByLocationId(locationId);
    }

    @Override
    public boolean isAllowedToAccessPost(PostDTO post, User currentUser) {
        Couple couple = coupleService.getCoupleByUser(currentUser);
        return couple != null && couple.getCoupleID().equals(post.getCoupleID());
    }

    @Override
    public boolean isPostOwner(Integer postId, User currentUser) {
        Post post = postMapper.findById(postId);
        return post != null && post.getUserID().equals(currentUser.getUserID());
    }

    private PostDTO convertToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        BeanUtils.copyProperties(post, postDTO);

        postDTO.setDeleted(post.isIsDeleted());
        
        // 获取最新的用户信息
        User user = userService.getUserById(post.getUserID());
        postDTO.setUserName(user.getUserName());
        postDTO.setNickName(user.getNickName());
        postDTO.setUserAvatar(user.getAvatar());
        
        // 设置图片列表
        List<ImageDTO> images = postMapper.findImagesByPostId(post.getPostID());
        postDTO.setImages(images);

        // 设置标签列表
        List<TagDTO> tags = postMapper.findTagsByPostId(post.getPostID());
        postDTO.setTags(tags);

        // 设置实体列表
        List<EntityDTO> entities = postMapper.findEntitiesByPostId(post.getPostID());
        postDTO.setEntities(entities);

        // 设置评论列表
        List<CommentDTO> comments = postMapper.findCommentsByPostId(post.getPostID());
        postDTO.setComments(comments);

        return postDTO;
    }
}
