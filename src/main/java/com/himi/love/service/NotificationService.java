package com.himi.love.service;

public interface NotificationService {
    void sendBarkNotification(String barkToken, String title, String body);
    void sendDailyReminder();
}
