package com.project.ContentPublishing.service.User;

import com.project.ContentPublishing.model.PlatformSettings;
import com.project.ContentPublishing.model.Roles;
import com.project.ContentPublishing.model.User;
import com.project.ContentPublishing.repository.PlatformSettingsRepository;
import com.project.ContentPublishing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlatformSettingsRepository platformSettingsRepository;

    @Override
    public void run(ApplicationArguments args) {
        createUser("admin", "admin@test.com", Roles.ADMIN);
        createUser("editor", "editor@test.com", Roles.EDITOR);
        createUser("author", "author@test.com", Roles.AUTHOR);
        createUser("reader", "reader@test.com", Roles.READER);
        createDefaultSettings();
    }

    private void createUser(String userName, String email, Roles role) {
        if (userRepository.findByEmail(email).isEmpty()) {
            userRepository.save(User.builder()
                    .userName(userName)
                    .email(email)
                    .passwordHash(passwordEncoder.encode("password123"))
                    .role(role)
                    .enabled(true)
                    .build());
            System.out.println("[INIT] Created " + role + " user: " + email);
        }
    }

    private void createDefaultSettings() {
        if (platformSettingsRepository.findFirstBy().isEmpty()) {
            platformSettingsRepository.save(PlatformSettings.builder()
                    .siteName("Content Publishing Platform")
                    .allowRegistration(true)
                    .maxArticlesPerAuthor(10)
                    .maintenanceMode(false)
                    .build());
            System.out.println("[INIT] Created default platform settings");
        }
    }
}