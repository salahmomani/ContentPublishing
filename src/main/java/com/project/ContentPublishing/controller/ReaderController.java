package com.project.ContentPublishing.controller;

import com.project.ContentPublishing.dto.Request.CommentRequest;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.model.User;
import com.project.ContentPublishing.service.User.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;

    @GetMapping("/browsesContent")
    public List<ArticleResponse> browsesContent() {
        return readerService.browsesContent();
    }

    @GetMapping("/viewArticle/{articleId}")
    public ArticleResponse viewArticle(@PathVariable Long articleId) {
        return readerService.viewArticle(articleId);
    }

    @PostMapping("/likeArticle/{articleId}")
    public void likeArticle(
            @PathVariable Long articleId,
            @RequestBody User user) {
        readerService.likeArticle(articleId, user.getId());
    }

    @PostMapping("/addComment/{articleId}")
    public void addComment(
            @PathVariable Long articleId,
            @PathVariable Long userId,
            @RequestBody CommentRequest request,
            @RequestBody User user) {
        readerService.addComment(articleId, userId, request);
    }

    @DeleteMapping("/removeComment/{commentId}")
    public void removeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user) {
        readerService.removeComment(commentId, user.getId());
    }
}