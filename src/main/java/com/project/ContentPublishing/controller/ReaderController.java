package com.project.ContentPublishing.controller;

import com.project.ContentPublishing.dto.Request.CommentRequest;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.dto.Response.CommentResponse;
import com.project.ContentPublishing.model.User;
import com.project.ContentPublishing.security.AuthUtils;
import com.project.ContentPublishing.service.User.ReaderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;
    private final AuthUtils authUtils;

    @GetMapping
    @PreAuthorize("hasRole('READER')")
    public List<ArticleResponse> browsesContent() {
        return readerService.browsesContent();
    }

    @PreAuthorize("hasRole('READER')")
    @GetMapping("/{articleId}")
    public ArticleResponse viewArticle(@PathVariable Long articleId) {
        return readerService.viewArticle(articleId);
    }

    @PreAuthorize("hasRole('READER')")
    @PostMapping("/{articleId}/like")
    public void likeArticle(
            @PathVariable Long articleId,
            HttpServletRequest httpRequest) {
        readerService.likeArticle(articleId, authUtils.getCurrentUserId(httpRequest));
    }

    @PreAuthorize("hasRole('READER')")
    @PostMapping("/{articleId}/comments")
    public CommentResponse addComment(
            @PathVariable Long articleId,
            @RequestBody @Valid CommentRequest request,
            HttpServletRequest httpRequest) {
        return readerService.addComment(articleId, authUtils.getCurrentUserId(httpRequest), request);
    }

    @PreAuthorize("hasRole('READER')")
    @DeleteMapping("/comments/{commentId}")
    public void removeComment(
            @PathVariable Long commentId,
            HttpServletRequest httpRequest) {
        readerService.removeComment(commentId, authUtils.getCurrentUserId(httpRequest));
    }
}