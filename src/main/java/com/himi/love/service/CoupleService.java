package com.himi.love.service;

import com.himi.love.model.Couple;
import com.himi.love.model.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CoupleService {
    Couple createCouple(Integer userID1, Integer userID2, LocalDate anniversaryDate, MultipartFile background, User currentUser);
    Couple getCoupleById(Integer coupleId);
    Couple getCoupleByUser(User currentUser);
    List<Couple> getAllCouples();
    Couple updateCouple(User currentUser, Integer coupleId, LocalDate anniversaryDate, MultipartFile background);
    void deleteCouple(Integer coupleId);
    boolean areCoupled(Integer userID1, Integer userID2);
    boolean checkCouplePermission(Integer coupleId, User currentUser);
}
