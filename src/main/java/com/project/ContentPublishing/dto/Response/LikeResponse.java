package com.project.ContentPublishing.dto.Response;

import lombok.Getter;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponse {

    private Long id;
    private String username;
    private Long articleId;
    private String articleTitle;
    private LocalDateTime createdAt;
}
