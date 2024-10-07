package com.himi.love.mapper;

import com.himi.love.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    User findById(Integer userId);
    User findByEmail(String email);
    List<User> findAll();
    int insert(User user);
    int update(User user);
    int deleteById(Integer userId);
    int softDeleteById(Integer userId);
    int updateLastLoginTime(Integer userId);
    long count();
}