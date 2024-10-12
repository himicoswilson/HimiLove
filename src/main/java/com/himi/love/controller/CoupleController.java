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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
@RestController
@RequestMapping("/api/couples")
public class CoupleController {

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Couple> createCouple(@RequestPart("couple") Couple couple,
                                               @RequestPart(value = "background", required = false) MultipartFile background,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        if (!currentUser.getUserID().equals(couple.getUserID1()) && !currentUser.getUserID().equals(couple.getUserID2())) {
            return ResponseEntity.badRequest().build();
        }
        Couple createdCouple = coupleService.createCouple(couple.getUserID1(), couple.getUserID2(), couple.getAnniversaryDate(), background, currentUser);
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

    @PutMapping(value = "/{coupleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Couple> updateCouple(@PathVariable Integer coupleId,
                                               @RequestParam(value = "anniversaryDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate anniversaryDate,
                                               @RequestPart(value = "background", required = false) MultipartFile background,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        Couple existingCouple = coupleService.getCoupleById(coupleId);
        if (existingCouple == null || (!currentUser.getUserID().equals(existingCouple.getUserID1()) && !currentUser.getUserID().equals(existingCouple.getUserID2()))) {
            return ResponseEntity.badRequest().build();
        }
        Couple updatedCouple = coupleService.updateCouple(currentUser, coupleId, anniversaryDate, background);
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
