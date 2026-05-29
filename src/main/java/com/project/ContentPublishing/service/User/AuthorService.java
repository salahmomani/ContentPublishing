package com.project.ContentPublishing.service.User;

import com.project.ContentPublishing.Exception.ResourceNotFoundException;
import com.project.ContentPublishing.dto.Request.ArticleRequest;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.mapper.ArticleMapper;
import com.project.ContentPublishing.model.Article;
import com.project.ContentPublishing.model.ArticleStatus;
import com.project.ContentPublishing.model.User;
import com.project.ContentPublishing.repository.ArticleRepository;
import com.project.ContentPublishing.repository.CommentRepository;
import com.project.ContentPublishing.repository.LikeRepository;
import com.project.ContentPublishing.repository.UserRepository;
import com.project.ContentPublishing.service.Notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
private final NotificationService notificationService;

    private Article getOwnArticle(Long articleId, Long authorId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        return article;
    }

    @PreAuthorize("hasRole('Author')")
    public ArticleResponse createArticle(ArticleRequest articleRequest, Long authorId) {
        User author = userRepository.findById(Math.toIntExact(authorId)).orElseThrow(() ->
                new ResourceNotFoundException("Author not found"));
        Article article = Article.builder().
                title(articleRequest.getTitle()).
                body(articleRequest.getBody()).
                excerpt(articleRequest.getExcerpt()).
                status(ArticleStatus.DRAFT).
                createdAt(LocalDateTime.now()).build();
        return articleMapper.toDto(articleRepository.save(article));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    public ArticleResponse updateArticle(Long articleId, ArticleRequest request, Long authorId) {
        Article article = Article.builder().
                title(request.getTitle()).
                excerpt(request.getExcerpt()).
                body(request.getBody()).
                status(ArticleStatus.PUBLISHED).
                updatedAt(LocalDateTime.now()).build();

        if (article.getStatus() == ArticleStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot edit a published article");
        }

        article.setTitle(request.getTitle());
        article.setUpdatedAt(LocalDateTime.now());

        return articleMapper.toDto(articleRepository.save(article));
    }

    @PreAuthorize("hasRole('AUTHOR')")
    public void deleteArticle(Long articleId, Long authorId) {
        Article article = getOwnArticle(articleId, authorId);
        articleRepository.delete(article);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    public ArticleResponse submitForReview(Long articleId, Long authorId) {
        Article article = getOwnArticle(articleId, authorId);

        if (article.getStatus() != ArticleStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT articles can be submitted for review");
        }

        article.setStatus(ArticleStatus.UNDER_REVIEW);
        article.setUpdatedAt(LocalDateTime.now());

        Article saved = articleRepository.save(article);

        notificationService.notifyEditorsArticleSubmitted(saved);

        return articleMapper.toDto(saved);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    public List<ArticleResponse> getMyArticles(Long authorId) {
        userRepository.findById(Math.toIntExact(authorId))
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        return articleRepository.findByAuthorId(authorId)
                .stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }


}
