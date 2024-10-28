package com.himi.love.service.impl;

import com.himi.love.model.Couple;
import com.himi.love.model.User;
import com.himi.love.service.NotificationService;
import com.himi.love.service.CoupleService;
import com.himi.love.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Random;


@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private CoupleService coupleService;
    
    @Autowired
    private UserService userService;
    
    @Value("${bark.default.group}")
    private String defaultGroup;
    
    @Value("${bark.default.icon}")
    private String defaultIcon;
    
    @Value("${bark.default.scheme}")
    private String defaultScheme;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Override
    public void sendBarkNotification(String barkToken, String title, String body) {
        if (!StringUtils.hasText(barkToken)) {
            return;
        }
        
        try {
            String url = "https://api.day.app/" + barkToken;
            
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .pathSegment(title, body)  // 使用 pathSegment 让 UriComponentsBuilder 自动处理编码
                .queryParam("group", defaultGroup)
                .queryParam("icon", defaultIcon)
                .queryParam("url", defaultScheme);
                
            System.out.println("发送Bark通知: " + builder.toUriString());
            restTemplate.getForEntity(builder.build().encode().toUri(), String.class);
        } catch (Exception e) {
            System.out.println("发送Bark通知失败: " + e.getMessage());
        }
    }
    
    @Scheduled(cron = "0 0 10,20 * * ?") // 每天上午10点和晚上8点执行
    @Override
    public void sendDailyReminder() {
        List<Couple> couples = coupleService.getAllCouples();
        System.out.println("发送每日提醒，共有 " + couples.size() + " 对情侣");
        
        // 随机选择一条提醒消息
        String[] reminderMessages = {
            "今天发生了什么有趣的事情呢？记得和爱人分享哦~ 💕",
            "记录一下今天的心情吧，让爱人知道你在想什么 😊",
            "今天和爱人说过我爱你了吗？别忘了表达爱意哦 ❤️",
            "分享今天的快乐时光，留下美好的回忆 📝",
            "今天也要好好记录生活中的点点滴滴呢 ✨",
            "来记录一下今天的生活吧，让回忆更加珍贵 🌟",
            "今天过得怎么样？记得分享给你的另一半哦 💑",
            "记录下此刻的心情，让爱更有温度 💖"
        };
        
        Random random = new Random();
        String message = reminderMessages[random.nextInt(reminderMessages.length)];
        
        for (Couple couple : couples) {
            User user1 = userService.getUserById(couple.getUserID1());
            System.out.println("user1: " + user1.getUserName());
            User user2 = userService.getUserById(couple.getUserID2());
            System.out.println("user2: " + user2.getUserName());
            
            // 给情侣双方都发送提醒
            if (user1 != null && StringUtils.hasText(user1.getBarkToken())) {
                System.out.println("user1: " + user1.getBarkToken());
                sendBarkNotification(user1.getBarkToken(), "甜蜜提醒", message);
            }
            
            if (user2 != null && StringUtils.hasText(user2.getBarkToken())) {
                System.out.println("user2: " + user2.getBarkToken());
                sendBarkNotification(user2.getBarkToken(), "甜蜜提醒", message);
            }
        }
    }
}
