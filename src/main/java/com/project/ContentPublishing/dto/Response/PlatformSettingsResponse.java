package com.project.ContentPublishing.dto.Response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformSettingsResponse {
    private Long id;
    private String siteName;
    private boolean allowRegistration;
    private int maxArticlesPerAuthor;
    private boolean maintenanceMode;
}