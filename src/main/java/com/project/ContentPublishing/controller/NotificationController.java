package com.project.ContentPublishing.controller;

import com.project.ContentPublishing.model.Notification;
import com.project.ContentPublishing.model.User;
import com.project.ContentPublishing.service.Notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/getMyNotifications")
    public List<Notification> getMyNotifications(
            @AuthenticationPrincipal User user) {
        return notificationService.getMyNotifications(user.getId());
    }

    @PatchMapping("/{notificationId}/read")
    public void markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal User user) {
        notificationService.markAsRead(notificationId, user.getId());
    }
}