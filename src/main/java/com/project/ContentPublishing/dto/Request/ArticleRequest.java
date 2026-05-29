package com.project.ContentPublishing.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ArticleRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Body is required")
    private String body;

    @Size(max = 500)
    private String excerpt;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private Set<Long> tagIds;

    private List<String> tags;

    private String status;

}