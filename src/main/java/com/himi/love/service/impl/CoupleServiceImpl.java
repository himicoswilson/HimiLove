package com.himi.love.service.impl;

import com.himi.love.mapper.CoupleMapper;
import com.himi.love.model.Couple;
import com.himi.love.model.User;
import com.himi.love.service.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CoupleServiceImpl implements CoupleService {

    @Autowired
    private CoupleMapper coupleMapper;

    @Override
    @Transactional
    @CacheEvict(cacheNames = "coupleByUser", key = "#currentUser.getUserID()")
    public Couple createCouple(Integer userID1, Integer userID2, LocalDate anniversaryDate, User currentUser) {
        if (!currentUser.getUserID().equals(userID1) && !currentUser.getUserID().equals(userID2)) {
            throw new RuntimeException("没有权限创建这对情侣关系");
        }
        if (areCoupled(userID1, userID2)) {
            throw new RuntimeException("这两个用户已经是情侣关系了");
        }
        Couple couple = new Couple(userID1, userID2, anniversaryDate);
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
    @CacheEvict(cacheNames = "couple", key = "#couple.getCoupleID()")
    public Couple updateCouple(Couple couple) {
        coupleMapper.update(couple);
        return couple;
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
    @Transactional
    public void updateAnniversaryDate(Integer coupleId, LocalDate newAnniversaryDate) {
        Couple couple = getCoupleById(coupleId);
        if (couple == null) {
            throw new RuntimeException("找不到指定的情侣关系");
        }
        couple.setAnniversaryDate(newAnniversaryDate);
        updateCouple(couple);
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
