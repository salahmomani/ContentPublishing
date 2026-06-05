package com.project.ContentPublishing.dto.Request;

import com.project.ContentPublishing.model.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleFilterRequest {

    private Long categoryId;

    private String tag;

    private Long authorId;

    private ArticleStatus status;

    private String keyword;
}