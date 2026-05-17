package com.project.ContentPublishing.dto.Response;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ArticleResponse {

    private Long id;

    private String title;

    private String slug;

    private String body;

    private String excerpt;

    private String category;

    private Set<String> tags;

    private String status;

    private String author;

    private LocalDateTime publishedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}