package com.project.ContentPublishing.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "platform_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String siteName;

    @Column(nullable = false)
    private boolean allowRegistration;

    @Column(nullable = false)
    private int maxArticlesPerAuthor;

    @Column(nullable = false)
    private boolean maintenanceMode;
}