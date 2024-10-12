package com.himi.love.service.impl;

import com.himi.love.mapper.CoupleMapper;
import com.himi.love.model.Couple;
import com.himi.love.model.User;
import com.himi.love.service.CoupleService;
import com.himi.love.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

@Service
public class CoupleServiceImpl implements CoupleService {

    @Autowired
    private CoupleMapper coupleMapper;

    @Autowired
    private ImageUploadService imageUploadService;

    @Override
    @Transactional
    public Couple createCouple(Integer userID1, Integer userID2, LocalDate anniversaryDate, MultipartFile background, User currentUser) {
        // 检查用户是否已经在一个情侣关系中
        if (coupleMapper.findByUserId(userID1) != null || coupleMapper.findByUserId(userID2) != null) {
            throw new RuntimeException("One or both users are already in a couple relationship");
        }

        Couple couple = new Couple();
        couple.setUserID1(userID1);
        couple.setUserID2(userID2);
        couple.setAnniversaryDate(anniversaryDate);

        // 上传背景图片（如果提供）
        if (background != null && !background.isEmpty()) {
            String backgroundUrl = imageUploadService.uploadCoupleBg(background, couple);
            couple.setBgImg(backgroundUrl);
        }

        coupleMapper.insert(couple);
        return couple;
    }

    @Override
    @Cacheable(cacheNames = "couple", key = "#coupleId", unless = "#result == null")
    public Couple getCoupleById(Integer coupleId) {
        return coupleMapper.findById(coupleId);
    }

    @Override
    @Cacheable(cacheNames = "coupleByUser", key = "#currentUser.getUserID()", unless = "#result == null")
    public Couple getCoupleByUser(User currentUser) {
        return coupleMapper.findByUserId(currentUser.getUserID());
    }

    @Override
    @Cacheable(cacheNames = "couples", unless = "#result.isEmpty()")
    public List<Couple> getAllCouples() {
        return coupleMapper.findAll();
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = "couple", key = "#coupleId"),
        @CacheEvict(cacheNames = "coupleByUser", key = "#currentUser.getUserID()")
    })
    public Couple updateCouple(User currentUser, Integer coupleId, LocalDate anniversaryDate, MultipartFile background) {
        Couple existingCouple = coupleMapper.findById(coupleId);
        if (existingCouple == null) {
            throw new RuntimeException("Couple not found");
        }

        boolean isUpdated = false;

        // 更新纪念日
        if (anniversaryDate != null) {
            existingCouple.setAnniversaryDate(anniversaryDate);
            isUpdated = true;
        }

        // 更新背景图片
        if (background != null && !background.isEmpty()) {
            String backgroundUrl = imageUploadService.uploadCoupleBg(background, existingCouple);
            existingCouple.setBgImg(backgroundUrl);
            isUpdated = true;
        }

        // 只有在至少有一个字段被更新时才执行更新操作
        if (isUpdated) {
            coupleMapper.update(existingCouple);
        } else {
            throw new IllegalArgumentException("至少需要提供纪念日或背景图片中的一个进行更新");
        }

        return existingCouple;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "couple", key = "#coupleId")
    public void deleteCouple(Integer coupleId) {
        coupleMapper.deleteById(coupleId);
    }

    @Override
    public boolean areCoupled(Integer userID1, Integer userID2) {
        Couple couple = coupleMapper.findByUserIds(userID1, userID2);
        return couple != null;
    }

    @Override
    public boolean checkCouplePermission(Integer coupleId, User currentUser) {
        Couple couple = getCoupleById(coupleId);
        if (couple == null) {
            return false;
        }
        return currentUser.getUserID().equals(couple.getUserID1()) || currentUser.getUserID().equals(couple.getUserID2());
    }
}
