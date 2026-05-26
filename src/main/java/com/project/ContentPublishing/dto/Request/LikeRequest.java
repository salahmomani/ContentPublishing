package com.project.ContentPublishing.dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeRequest {

    @NotNull(message = "Article ID is required")
    private Long articleId;
}