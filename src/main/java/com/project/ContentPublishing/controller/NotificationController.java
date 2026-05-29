package com.project.ContentPublishing.controller;

import com.project.ContentPublishing.model.Notification;
import com.project.ContentPublishing.model.User;
import com.project.ContentPublishing.service.Notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<Notification> getMyNotifications(
            @RequestBody User user) {
        return notificationService.getMyNotifications(user.getId());
    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(
            @PathVariable Long notificationId,
            @RequestBody User user) {
        notificationService.markAsRead(notificationId, user.getId());
    }
}