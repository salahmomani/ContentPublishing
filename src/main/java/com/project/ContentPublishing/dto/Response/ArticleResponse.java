package com.project.ContentPublishing.dto.Response;


import com.project.ContentPublishing.model.ArticleCategory;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ArticleResponse {

    private Long id;

    private String title;

    private String slug;

    private String body;

    private String excerpt;

    private String category;

    private List<String> tags;

    private String status;

    private String author;

    private LocalDateTime publishedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int commentCount;

}