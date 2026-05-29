package com.project.ContentPublishing.dto.Response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private String authorName;
    private Long articleId;
    private String articleTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}