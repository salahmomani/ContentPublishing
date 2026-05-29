package com.project.ContentPublishing.service.User;

import com.project.ContentPublishing.Exception.ResourceNotFoundException;
import com.project.ContentPublishing.dto.Request.CategoryRequest;
import com.project.ContentPublishing.dto.Request.PlatformSettingsRequest;
import com.project.ContentPublishing.dto.Response.CategoryResponse;
import com.project.ContentPublishing.dto.Response.PlatformSettingsResponse;
import com.project.ContentPublishing.dto.Response.UserResponse;
import com.project.ContentPublishing.mapper.CategoryMapper;
import com.project.ContentPublishing.mapper.PlatformSettingsMapper;
import com.project.ContentPublishing.mapper.UserMapper;
import com.project.ContentPublishing.model.ArticleCategory;
import com.project.ContentPublishing.model.PlatformSettings;
import com.project.ContentPublishing.model.Roles;
import com.project.ContentPublishing.model.User;
import com.project.ContentPublishing.repository.CategoryRepository;
import com.project.ContentPublishing.repository.PlatformSettingsRepository;
import com.project.ContentPublishing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PlatformSettingsRepository settingsRepository;
    private final PlatformSettingsMapper platformSettingsMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse changeUserRole(Long userId, Roles newRole) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRole(newRole);
        return userMapper.toDto(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse banUser(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new IllegalStateException("User is already banned");
        }

        user.setEnabled(
                false);
        return userMapper.toDto(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalStateException("Category already exists");
        }

        ArticleCategory category = ArticleCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {
        ArticleCategory category = (ArticleCategory) categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Long categoryId) {
        ArticleCategory category = (ArticleCategory) categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PlatformSettingsResponse getSettings() {
        return settingsRepository.findFirstBy()
                .map(platformSettingsMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PlatformSettingsResponse updateSettings(PlatformSettingsRequest request) {
        PlatformSettings settings = settingsRepository.findFirstBy()
                .orElse(new PlatformSettings());

        settings.setSiteName(request.getSiteName());
        settings.setAllowRegistration(request.isAllowRegistration());
        settings.setMaxArticlesPerAuthor(request.getMaxArticlesPerAuthor());
        settings.setMaintenanceMode(request.isMaintenanceMode());

        return platformSettingsMapper.toDto(settingsRepository.save(settings));
    }
}
