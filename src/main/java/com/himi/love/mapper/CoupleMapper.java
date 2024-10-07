package com.himi.love.mapper;

import com.himi.love.model.Couple;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoupleMapper {
    Couple findById(Integer coupleId);
    List<Couple> findAll();
    Couple findByUserId(Integer userId);
    Couple findByUserIds(Integer userID1, Integer userID2);
    int insert(Couple couple);
    int update(Couple couple);
    int deleteById(Integer coupleId);
}