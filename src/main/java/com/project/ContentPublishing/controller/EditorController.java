package com.project.ContentPublishing.controller;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.dto.Response.CommentResponse;
import com.project.ContentPublishing.service.User.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/editor")
@RequiredArgsConstructor
public class EditorController {

    private final EditorService editorService;

    @GetMapping("/getPendingArticles/pending")
    public List<ArticleResponse> getPendingArticles() {
        return editorService.getPendingArticles();
    }

    @GetMapping("/reviewArticle/{articleId}")
    public ArticleResponse reviewArticle(@PathVariable Long articleId) {
        return editorService.reviewArticle(articleId);
    }

    @PostMapping("/publishArticle/{articleId}")
    public ArticleResponse publishArticle(@PathVariable Long articleId) {
        return editorService.publishArticle(articleId);
    }

    @PostMapping("/rejectArticle/{articleId}")
    public ArticleResponse rejectArticle(
            @PathVariable Long articleId,
            @RequestParam String reason) {
        return editorService.rejectArticle(articleId, reason);
    }

    @GetMapping("/getAllComments/{articleId}")
    public List<CommentResponse> getAllComments(@PathVariable Long articleId) {
        return editorService.getAllComments(articleId);
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        editorService.deleteComment(commentId);
    }
}