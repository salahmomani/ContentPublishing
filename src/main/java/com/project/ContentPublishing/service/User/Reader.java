package com.project.ContentPublishing.service.User;

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
public class Reader {
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
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        Comment comment = Comment.builder()
                .article(article).
                id(articleId)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }

    @PreAuthorize("hasRole('READER')")
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
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        boolean alreadyLiked = likeRepository.existsByArticleIdAndUserId(articleId, userId);
        if (alreadyLiked) {
            throw new IllegalStateException("Article already liked");
        }

        Like like = new Like(article, userId);
        likeRepository.save(like);
    }

    @PreAuthorize("hasRole('READER')")
    public ArticleResponse viewArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        return articleMapper.toDto(article);
    }
}
