package com.himi.love.service;

import com.himi.love.model.Couple;
import com.himi.love.model.User;
import java.time.LocalDate;
import java.util.List;

public interface CoupleService {
    Couple createCouple(Integer userID1, Integer userID2, LocalDate anniversaryDate, User currentUser);
    Couple getCoupleById(Integer coupleId);
    Couple getCoupleByUser(User currentUser);
    List<Couple> getAllCouples();
    Couple updateCouple(Couple couple);
    void deleteCouple(Integer coupleId);
    boolean areCoupled(Integer userID1, Integer userID2);
    void updateAnniversaryDate(Integer coupleId, LocalDate newAnniversaryDate);
    boolean checkCouplePermission(Integer coupleId, User currentUser);
}