package com.project.ContentPublishing.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class PlatformSettingsRequest {

    @NotBlank(message = "Site name is required")
    private String siteName;

    private boolean allowRegistration;
    private int maxArticlesPerAuthor;
    private boolean maintenanceMode;
}

