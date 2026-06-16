package com.project.ContentPublishing.controller;

import com.project.ContentPublishing.dto.Request.CategoryRequest;
import com.project.ContentPublishing.dto.Request.PlatformSettingsRequest;
import com.project.ContentPublishing.dto.Response.CategoryResponse;
import com.project.ContentPublishing.dto.Response.PlatformSettingsResponse;
import com.project.ContentPublishing.dto.Response.UserResponse;
import com.project.ContentPublishing.model.Roles;
import com.project.ContentPublishing.service.User.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUsers();

    }

    @PutMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse changeUserRole(@PathVariable Long userId, @RequestBody Roles newRole) {
        return adminService.changeUserRole(userId, newRole);
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
    }


    @PutMapping("/users/{userId}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse banUser(@PathVariable Long userId) {
        return adminService.banUser(userId);
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(@RequestBody CategoryRequest request) {
        return adminService.createCategory(request);
    }


    @PutMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequest request) {

        return adminService.updateCategory(categoryId, request);
    }

    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable Long categoryId) {
        adminService.deleteCategory(categoryId);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponse> getAllCategories() {
        return adminService.getAllCategories();
    }

    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public PlatformSettingsResponse getSettings() {
        return adminService.getSettings();
    }

    @PutMapping("/updateSettings")
    @PreAuthorize("hasRole('ADMIN')")
    public PlatformSettingsResponse updateSettings(@RequestBody PlatformSettingsRequest request) {
        return adminService.updateSettings(request);
    }
}