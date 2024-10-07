package com.himi.love.service;

import com.himi.love.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
    
    // 用户注册
    User register(User user);
    
    // 用户登录
    User login(String username, String password);
    
    // 用户登出
    void logout(String token);
    
    // 删除用户（软删除）
    void deleteUser(Integer userId);
    
    // 更新用户信息
    User updateUser(User user);
    
    // 根据ID获取用户
    User getUserById(Integer userId);
    
    // 根据用户名获取用户
    User getUserByUsername(String username);
    
    // 根据邮箱获取用户
    User getUserByEmail(String email);
    
    // 获取所有用户
    List<User> getAllUsers();
    
    // 修改密码
    void changePassword(Integer userId, String oldPassword, String newPassword);
    
    // 重置密码（忘记密码功能）
    void resetPassword(String email);
    
    // 检查用户名是否可用
    boolean isUsernameAvailable(String username);
    
    // 检查邮箱是否可用
    boolean isEmailAvailable(String email);
    
    // 更新用户头像
    User updateAvatar(Integer userId, String avatarUrl);
    
    // 获取用户数量
    long getUserCount();
    
    // 检查用户是否存在
    boolean existsById(Integer userId);
}