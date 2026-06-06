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
import com.project.ContentPublishing.service.SlugUtil.SlugUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    private final NotificationService notificationService;
    private final SlugUtil slugUtil;

    private Article getOwnArticle(Long articleId, Long authorId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        return article;
    }

    @CacheEvict(value = {"published-articles", "articles-by-category", "articles-by-tag"}, allEntries = true)
    @Transactional
    public ArticleResponse createArticle(ArticleRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Article article = Article.builder()
                .title(request.getTitle())
                .slug(slugUtil.generateUniqueSlug(request.getTitle()))
                .body(request.getBody())
                .excerpt(request.getExcerpt())
                .tags(request.getTags())
                .author(author)
                .status(ArticleStatus.DRAFT)
                .build();

        return articleMapper.toDto(articleRepository.save(article));
    }

    @CacheEvict(value = {"published-articles", "article", "articles-by-category", "articles-by-tag"}, allEntries = true)
    @Transactional
    public ArticleResponse updateArticle(Long articleId, ArticleRequest request, Long authorId) {
        Article article = getOwnArticle(articleId, authorId);  // 👈 fetch from DB

        switch (article.getStatus()) {
            case UNDER_REVIEW -> throw new IllegalStateException("Cannot edit while UNDER_REVIEW");
            case ARCHIVED -> throw new IllegalStateException("Cannot edit an ARCHIVED article");
            case PUBLISHED -> {
                return createDraftCopy(article, request, authorId);
            }
            default -> {
                article.setTitle(request.getTitle());
                article.setBody(request.getBody());
                article.setExcerpt(request.getExcerpt());
                article.setSlug(slugUtil.generateUniqueSlug(request.getTitle()));
                article.setUpdatedAt(LocalDateTime.now());
                return articleMapper.toDto(articleRepository.save(article));
            }
        }
    }


    private ArticleResponse createDraftCopy(Article published, ArticleRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        Article draft = Article.builder()
                .title(request.getTitle())
                .slug(slugUtil.generateUniqueSlug(request.getTitle()))
                .body(request.getBody())
                .excerpt(request.getExcerpt())
                .category(published.getCategory())
                .author(author)
                .status(ArticleStatus.DRAFT)
                .build();

        return articleMapper.toDto(articleRepository.save(draft));
    }

    @CacheEvict(value = {"published-articles", "article", "articles-by-category", "articles-by-tag"}, allEntries = true)
    @Transactional
    public void deleteArticle(Long articleId, Long authorId) {
        Article article = getOwnArticle(articleId, authorId);
        articleRepository.delete(article);
    }

    @CacheEvict(value = {"published-articles", "article", "articles-by-category", "articles-by-tag"}, allEntries = true)
    @Transactional
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

    @Cacheable(value = "my-articles", key = "#authorId")
    public List<ArticleResponse> getMyArticles(Long authorId) {
        userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

        return articleRepository.findByAuthorId(authorId)
                .stream()
                .map(articleMapper::toDto)
                .collect(Collectors.toList());
    }


}
