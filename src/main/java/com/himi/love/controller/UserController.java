package com.himi.love.controller;

import com.himi.love.model.User;
import com.himi.love.service.CoupleService;
import com.himi.love.service.UserService;
import com.himi.love.config.JwtConfig;
import com.himi.love.dto.AuthResponse;
import com.himi.love.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CoupleService coupleService;

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        String token = jwtConfig.generateToken(registeredUser.getUserName());
        return ResponseEntity.ok(new AuthResponse(token, registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            String token = jwtConfig.generateToken(user.getUserName());
            return ResponseEntity.ok(new AuthResponse(token, user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId, @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId, @RequestBody User user, @AuthenticationPrincipal UserDetails userDetails) {
        user.setUserID(userId);
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PutMapping("/{userId}/avatar")
    public ResponseEntity<?> updateUserAvatar(@PathVariable Integer userId, @RequestParam("avatar") MultipartFile avatar,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        User targetUser = userService.getUserById(userId);

        if (targetUser == null) {
            return ResponseEntity.status(404).body("用户不存在");
        }

        // 检查是否为当前用户或其伴侣
        if (!currentUser.getUserID().equals(userId) && 
            (coupleService.getCoupleByUser(currentUser) == null || !coupleService.getCoupleByUser(currentUser).getUserID1().equals(targetUser.getUserID()) && !coupleService.getCoupleByUser(currentUser).getUserID2().equals(targetUser.getUserID()))) {
            return ResponseEntity.status(403).body("无权限修改该用户头像");
        }

        // 执行头像更新操作
        try {
            User updatedUser = userService.updateAvatar(userId, avatar);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("头像更新失败：" + e.getMessage());
        }
    }
}
