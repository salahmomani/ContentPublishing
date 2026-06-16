package com.project.ContentPublishing.controller;

import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.dto.Response.CommentResponse;
import com.project.ContentPublishing.service.User.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/editor")
@RequiredArgsConstructor
public class EditorController {

    private final EditorService editorService;

    @GetMapping("/getPendingArticles/pending")
    @PreAuthorize("hasRole('EDITOR')")
    public List<ArticleResponse> getPendingArticles() {
        return editorService.getPendingArticles();
    }

    @PreAuthorize("hasRole('EDITOR')")
    @GetMapping("/reviewArticle/{articleId}")
    public ArticleResponse reviewArticle(@PathVariable Long articleId) {
        return editorService.reviewArticle(articleId);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/publishArticle/{articleId}")
    public ArticleResponse publishArticle(@PathVariable Long articleId) {
        return editorService.publishArticle(articleId);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @PostMapping("/articles/{articleId}/reject")
    public ArticleResponse rejectArticle(
            @PathVariable Long articleId,
            @RequestParam String reason) {
        return editorService.rejectArticle(articleId, reason);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @GetMapping("/getAllComments/{articleId}")
    public List<CommentResponse> getAllComments(@PathVariable Long articleId) {
        return editorService.getAllComments(articleId);
    }

    @PreAuthorize("hasRole('EDITOR')")
    @DeleteMapping("/deleteComment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        editorService.deleteComment(commentId);
    }
}