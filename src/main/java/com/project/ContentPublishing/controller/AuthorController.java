package com.project.ContentPublishing.controller;


import com.project.ContentPublishing.dto.Request.ArticleRequest;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.security.AuthUtils;
import com.project.ContentPublishing.security.JwtService;
import com.project.ContentPublishing.service.Security.AuthService;
import com.project.ContentPublishing.service.User.AuthorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthorService authorService;
    private final AuthUtils authUtils;

    @PostMapping("/articles")
    public ArticleResponse createArticle(
            @RequestBody @Valid ArticleRequest request,
            HttpServletRequest httpRequest) {
        return authorService.createArticle(request, authUtils.getCurrentUserId(httpRequest));
    }

    @PutMapping("/{articleId}")
    public ArticleResponse updateArticle(
            @PathVariable Long articleId,
            @RequestBody @Valid ArticleRequest request,
            HttpServletRequest httpRequest) {
        return authorService.updateArticle(articleId, request, authUtils.getCurrentUserId(httpRequest));
    }

    @DeleteMapping("/{articleId}")
    public void deleteArticle(
            @PathVariable Long articleId,
            HttpServletRequest httpRequest) {
        authorService.deleteArticle(articleId, authUtils.getCurrentUserId(httpRequest));
    }

    @PostMapping("/{articleId}/submit")
    public ArticleResponse submitForReview(
            @PathVariable Long articleId,
            HttpServletRequest httpRequest) {
        return authorService.submitForReview(articleId, authUtils.getCurrentUserId(httpRequest));
    }

    @GetMapping
    public List<ArticleResponse> getMyArticles(HttpServletRequest httpRequest) {
        return authorService.getMyArticles(authUtils.getCurrentUserId(httpRequest));
    }

}