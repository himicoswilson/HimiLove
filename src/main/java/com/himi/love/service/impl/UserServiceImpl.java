package com.himi.love.service.impl;

import com.himi.love.service.UserService;
import com.himi.love.mapper.UserMapper;
import com.himi.love.model.Couple;
import com.himi.love.model.User;
import com.himi.love.config.JwtConfig;
import com.himi.love.service.ImageUploadService;
import com.himi.love.service.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public User register(User user) {
        if (!isUsernameAvailable(user.getUserName())) {
            throw new RuntimeException("用户名已存在");
        }
        if (!isEmailAvailable(user.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        userMapper.updateLastLoginTime(user.getUserID());
        return user;
    }

    @Override
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String username = jwtConfig.extractUsername(token);
        if (username != null) {
            // 将token加入黑名单
            jwtConfig.addToBlacklist(token);
        }
    }

    @Override
    @Cacheable(cacheNames = "users", key = "#userId")
    public User getUserById(Integer userId) {
        return userMapper.findById(userId);
    }

    @Override
    @CacheEvict(cacheNames = "users", key = "#user.getUserID()")
    @Transactional
    public User updateUser(User user) {
        User existingUser = userMapper.findById(user.getUserID());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }
        userMapper.update(user);
        return user;
    }

    @Override
    @CacheEvict(cacheNames = "users", key = "#userId")
    @Transactional
    public void deleteUser(Integer userId) {
        userMapper.softDeleteById(userId);
    }

    @Override
    @Cacheable(cacheNames = "users", key = "#username", unless = "#result == null")
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    @Cacheable(cacheNames = "users", key = "#email", unless = "#result == null")
    public User getUserByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    @Cacheable(cacheNames = "users", unless = "#result == null")
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    @Transactional
    public void changePassword(Integer userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.update(user);
    }

    @Override
    @Transactional
    public void resetPassword(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("该邮箱未注册");
        }
        // 生成随机密码
        String newPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.update(user);
        // 发送新密码到用户邮箱
        sendPasswordResetEmail(email, newPassword);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userMapper.findByUsername(username) == null;
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return userMapper.findByEmail(email) == null;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "users", key = "#userId")
    public User updateAvatar(Integer userId, MultipartFile avatar) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        String avatarUrl = imageUploadService.uploadUserAvatar(avatar, user);
        user.setAvatar(avatarUrl);
        userMapper.update(user);

        // 清除该用户相关的帖子缓存
        Couple couple = coupleService.getCoupleByUser(user);
        if (couple != null) {
            // 增加缓存版本号
            String versionKey = "posts:version:" + couple.getCoupleID();
            redisTemplate.opsForValue().increment(versionKey);

            // 只清除第一页缓存
            String firstPageCacheKey = couple.getCoupleID() + ":posts:1:10";
            redisTemplate.delete(firstPageCacheKey);
        }

        return user;
    }

    @Override
    public long getUserCount() {
        return userMapper.count();
    }

    @Override
    public boolean existsById(Integer userId) {
        return userMapper.findById(userId) != null;
    }

    // 辅助方法
    private String generateRandomPassword() {
        // 实现随机密码生成逻辑
        return "randomPassword";
    }

    private void sendPasswordResetEmail(String email, String newPassword) {
        // 实现发送邮件逻辑
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());
    }
}
