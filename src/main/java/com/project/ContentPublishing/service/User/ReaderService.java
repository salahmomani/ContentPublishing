package com.project.ContentPublishing.service.User;

import com.project.ContentPublishing.Exception.ResourceNotFoundException;
import com.project.ContentPublishing.dto.Request.CommentRequest;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.dto.Response.CommentResponse;
import com.project.ContentPublishing.mapper.ArticleMapper;
import com.project.ContentPublishing.mapper.CommentMapper;
import com.project.ContentPublishing.model.*;
import com.project.ContentPublishing.repository.ArticleRepository;
import com.project.ContentPublishing.repository.CommentRepository;
import com.project.ContentPublishing.repository.LikeRepository;
import com.project.ContentPublishing.repository.UserRepository;
import com.project.ContentPublishing.service.Notification.NotificationService;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReaderService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;
    private final CommentMapper commentMapper;

    @PreAuthorize("hasRole('READER')")
    public List<ArticleResponse> browsesContent() {
        List<Article> articles =
                articleRepository.findByStatus(ArticleStatus.PUBLISHED);

        return articles.stream()
                .map(articleMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('READER')")
    public CommentResponse addComment(Long articleId, Long userId, CommentRequest request) {
        Article article = articleRepository.findByIdAndStatus(articleId, ArticleStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found or not published"));

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .article(article)
                .user(user)
                .content(request.getContent())
                .build();

        commentRepository.save(comment);
        article.setCommentCount(article.getCommentCount() + 1);
        articleRepository.save(article);

        notificationService.notifyAuthorArticlePublished(article);

        return commentMapper.toDto(comment);
    }

    @PreAuthorize("hasRole('READER')")
    public void removeComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only delete your own comments");
        }
        Article article = comment.getArticle();
        article.setCommentCount(Math.max(0, article.getCommentCount() - 1));
        articleRepository.save(article);

        commentRepository.delete(comment);
    }

    @PreAuthorize("hasRole('READER')")
    public void likeArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        boolean alreadyLiked = likeRepository.existsByArticleIdAndUserId(articleId, userId);
        if (alreadyLiked) {
            throw new IllegalStateException("Article already liked");
        }

        Like like = Like.builder()
                .article(article)
                .user(userRepository.findById(Math.toIntExact(userId))
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")))
                .build();
        likeRepository.save(like);
    }

    @PreAuthorize("hasRole('READER')")
    public ArticleResponse viewArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        return articleMapper.toDto(article);
    }
}
