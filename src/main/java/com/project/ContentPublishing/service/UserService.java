package com.project.ContentPublishing.service;

import com.project.ContentPublishing.Exception.ResourceNotFoundException;
import com.project.ContentPublishing.dto.Request.ArticleRequest;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.mapper.ArticleMapper;
import com.project.ContentPublishing.model.*;
import com.project.ContentPublishing.repository.ArticleRepository;
import com.project.ContentPublishing.repository.CommentRepository;
import com.project.ContentPublishing.repository.LikeRepository;
import com.project.ContentPublishing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;


    @PreAuthorize("hasRole('Reader')")
    public List<ArticleResponse> browsesContent() {
        List<Article> articles =
                articleRepository.findByStatus(ArticleStatus.PUBLISHED);

        return articles.stream()
                .map(articleMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('READER')")
    public void addComment(Long articleId, Long userId, String content) {
        Article article = articleRepository.findById(Math.toIntExact(articleId))
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        Comment comment = Comment.builder()
                .article(article).
                id(articleId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }

    public void removeComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getId().equals(userId)) {
            throw new ResourceNotFoundException("Comment not found");
        }
        commentRepository.delete(comment);
    }

    @PreAuthorize("hasRole('READER')")
    public void likeArticle(Long articleId, Long userId) {
        Article article = articleRepository.findById(Math.toIntExact(articleId))
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        boolean alreadyLiked = likeRepository.existsByArticleIdAndUserId(articleId, userId);
        if (alreadyLiked) {
            throw new IllegalStateException("Article already liked");
        }

        Like like = new Like(article, userId);
        likeRepository.save(like);
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


}
