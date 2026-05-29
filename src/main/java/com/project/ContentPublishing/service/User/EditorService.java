package com.project.ContentPublishing.service.User;

import com.project.ContentPublishing.Exception.ResourceNotFoundException;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.dto.Response.CommentResponse;
import com.project.ContentPublishing.mapper.ArticleMapper;
import com.project.ContentPublishing.mapper.CommentMapper;
import com.project.ContentPublishing.model.Article;
import com.project.ContentPublishing.model.ArticleStatus;
import com.project.ContentPublishing.model.Comment;
import com.project.ContentPublishing.repository.ArticleRepository;
import com.project.ContentPublishing.repository.CommentRepository;
import com.project.ContentPublishing.repository.LikeRepository;
import com.project.ContentPublishing.repository.UserRepository;
import com.project.ContentPublishing.service.Notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EditorService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final NotificationService notificationService;

    private Article getArticleByStatus(Long articleId, ArticleStatus expectedStatus) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        if (article.getStatus() != expectedStatus) {
            throw new IllegalStateException(
                    "Article is not in " + expectedStatus + " status"
            );
        }
        return article;
    }

    @PreAuthorize("hasRole('EDITOR')")
    @Cacheable(value = "pending-articles")
    public List<ArticleResponse> getPendingArticles() {
        return articleRepository.findByStatus(ArticleStatus.UNDER_REVIEW)
                .stream()
                .map(articleMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('EDITOR')")
    @Cacheable(value = "article", key = "#articleId")
    public ArticleResponse reviewArticle(Long articleId) {
        Article article = getArticleByStatus(articleId, ArticleStatus.UNDER_REVIEW);
        return articleMapper.toDto(article);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @Cacheable(value = "article-comments", key = "#articleId")
    public List<CommentResponse> getAllComments(Long articleId) {
        articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        return commentRepository.findByArticleId(articleId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('EDITOR')")
    @CacheEvict(value = {"published-articles", "article", "pending-articles",
            "articles-by-category", "articles-by-tag"}, allEntries = true)
    public ArticleResponse publishArticle(Long articleId) {
        Article article = getArticleByStatus(articleId, ArticleStatus.UNDER_REVIEW);
        article.setStatus(ArticleStatus.PUBLISHED);
        article.setPublishedAt(LocalDateTime.now());
        Article savedArticle = articleRepository.save(article);
        notificationService.notifyAuthorArticlePublished(savedArticle);
        return articleMapper.toDto(savedArticle);

    }

    @PreAuthorize("hasRole('EDITOR')")
    @CacheEvict(value = {"published-articles", "article", "pending-articles",
            "articles-by-category", "articles-by-tag"}, allEntries = true)
    public ArticleResponse rejectArticle(Long articleId, String reason) {
        Article article = getArticleByStatus(articleId, ArticleStatus.UNDER_REVIEW);

        article.setStatus(ArticleStatus.REJECTED);
        article.setUpdatedAt(LocalDateTime.now());

        Article saved = articleRepository.save(article);

        notificationService.notifyAuthorArticleRejected(saved, reason);  // 👈 notify author

        return articleMapper.toDto(saved);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @CacheEvict(value = "article-comments", allEntries = true)
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        Article article = comment.getArticle();
        article.setCommentCount(Math.max(0, article.getCommentCount() - 1));
        articleRepository.save(article);

        commentRepository.delete(comment);
    }
}
