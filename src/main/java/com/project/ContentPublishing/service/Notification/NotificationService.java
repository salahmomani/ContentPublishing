package com.project.ContentPublishing.service.Notification;

import com.project.ContentPublishing.model.*;
import com.project.ContentPublishing.repository.NotificationRepository;
import com.project.ContentPublishing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void notifyAuthorArticlePublished(Article article) {
        Notification notification = Notification.builder()
                .recipient(article.getAuthor())
                .type(NotificationType.ARTICLE_PUBLISHED)
                .message("Your article has been published: " + article.getTitle())
                .read(false)
                .build();

        notificationRepository.save(notification);
        log.info("[NOTIFICATION] To: {} | Type: {} | Message: {}",
                notification.getRecipient().getUserName(),
                notification.getType(),
                notification.getMessage()
        );
    }

    public void notifyAuthorArticleRejected(Article article, String reason) {
        Notification notification = Notification.builder()
                .recipient(article.getAuthor())
                .type(NotificationType.ARTICLE_REJECTED)
                .message("Your article was rejected: " + article.getTitle() + ". Reason: " + reason)
                .read(false)
                .build();

        notificationRepository.save(notification);
        log.info("[NOTIFICATION] To: {} | Type: {} | Message: {}",
                notification.getRecipient().getUserName(),
                notification.getType(),
                notification.getMessage()
        );
    }
    public void notifyEditorsArticleSubmitted(Article article) {
        List<User> editors = userRepository.findByRole(Roles.Editor);

        editors.forEach(editor -> {
            Notification notification = Notification.builder()
                    .recipient(editor)
                    .type(NotificationType.ARTICLE_SUBMITTED)
                    .message("New article submitted for review: " + article.getTitle())
                    .read(false)
                    .build();

            notificationRepository.save(notification);
            log.info("[NOTIFICATION] To: {} | Type: {} | Message: {}",
                    editor.getUserName(),
                    notification.getType(),
                    notification.getMessage()
            );
        });
    }
}