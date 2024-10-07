package com.himi.love.controller;

import com.himi.love.model.Couple;
import com.himi.love.model.User;
import com.himi.love.service.CoupleService;
import com.himi.love.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/couples")
public class CoupleController {

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Couple> createCouple(@RequestBody Couple couple,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (!currentUser.getUserID().equals(couple.getUserID1()) && !currentUser.getUserID().equals(couple.getUserID2())) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Received couple: " + couple);
    System.out.println("AnniversaryDate: " + couple.getAnniversaryDate());
        Couple createdCouple = coupleService.createCouple(couple.getUserID1(), couple.getUserID2(), couple.getAnniversaryDate(), currentUser);
        return ResponseEntity.ok(createdCouple);
    }

    @GetMapping("/{coupleId}")
    public ResponseEntity<Couple> getCoupleById(@PathVariable Integer coupleId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        Couple couple = coupleService.getCoupleById(coupleId);
        if (couple == null || (!currentUser.getUserID().equals(couple.getUserID1()) && !currentUser.getUserID().equals(couple.getUserID2()))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(couple);
    }

    @PutMapping("/{coupleId}")
    public ResponseEntity<Couple> updateCouple(@PathVariable Integer coupleId,
                                               @RequestBody Couple couple,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        Couple existingCouple = coupleService.getCoupleById(coupleId);
        if (existingCouple == null || (!currentUser.getUserID().equals(existingCouple.getUserID1()) && !currentUser.getUserID().equals(existingCouple.getUserID2()))) {
            return ResponseEntity.badRequest().build();
        }
        couple.setCoupleID(coupleId);
        Couple updatedCouple = coupleService.updateCouple(couple);
        return ResponseEntity.ok(updatedCouple);
    }

    @DeleteMapping("/{coupleId}")
    public ResponseEntity<Void> deleteCouple(@PathVariable Integer coupleId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        Couple existingCouple = coupleService.getCoupleById(coupleId);
        if (existingCouple == null || (!currentUser.getUserID().equals(existingCouple.getUserID1()) && !currentUser.getUserID().equals(existingCouple.getUserID2()))) {
            return ResponseEntity.badRequest().build();
        }
        coupleService.deleteCouple(coupleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Couple> getCoupleByUser(@PathVariable Integer userId,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (!currentUser.getUserID().equals(userId)) {
            return ResponseEntity.badRequest().build();
        }
        Couple couple = coupleService.getCoupleByUser(currentUser);
        return ResponseEntity.ok(couple);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> areCoupled(@RequestBody Couple couple,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (!currentUser.getUserID().equals(couple.getUserID1()) && !currentUser.getUserID().equals(couple.getUserID2())) {
            return ResponseEntity.badRequest().build();
        }
        boolean areCoupled = coupleService.areCoupled(couple.getUserID1(), couple.getUserID2());
        return ResponseEntity.ok(areCoupled);
    }
}