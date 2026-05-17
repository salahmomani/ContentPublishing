package com.project.ContentPublishing.dto.Response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private String displayName;

    private String bio;

    private String role;

    private boolean enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
