package com.project.ContentPublishing.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String content;
}