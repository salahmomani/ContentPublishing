package com.project.ContentPublishing.controller;


import com.project.ContentPublishing.dto.Request.ArticleRequest;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.model.User;
import com.project.ContentPublishing.service.User.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ArticleResponse createArticle(
            @RequestBody ArticleRequest request,
            @RequestBody User user) {
        return authorService.createArticle(request, user.getId());
    }

    @PutMapping("/updateArticle/{articleId}")
    public ArticleResponse updateArticle(
            @PathVariable Long articleId,
            @RequestBody ArticleRequest request,
            @RequestBody User user) {
        return authorService.updateArticle(articleId, request, user.getId());
    }

    @DeleteMapping("/deleteArticle/{articleId}")
    public void deleteArticle(
            @PathVariable Long articleId,
            @RequestBody User user) {
        authorService.deleteArticle(articleId, user.getId());
    }

    @PostMapping("/submitForReview/{articleId}/submit")
    public ArticleResponse submitForReview(
            @PathVariable Long articleId,
            @RequestBody User user) {
        return authorService.submitForReview(articleId, user.getId());
    }

    @GetMapping("/getMyArticles")
    public List<ArticleResponse> getMyArticles(
            @RequestBody User user) {
        return authorService.getMyArticles(user.getId());
    }
}