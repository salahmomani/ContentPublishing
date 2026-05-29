package com.project.ContentPublishing.controller;

import com.project.ContentPublishing.dto.Request.CategoryRequest;
import com.project.ContentPublishing.dto.Request.PlatformSettingsRequest;
import com.project.ContentPublishing.dto.Response.CategoryResponse;
import com.project.ContentPublishing.dto.Response.PlatformSettingsResponse;
import com.project.ContentPublishing.dto.Response.UserResponse;
import com.project.ContentPublishing.model.Roles;
import com.project.ContentPublishing.service.User.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/getAll")
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUsers();

    }

    @PutMapping("/changeUserRole/{userId}")
    public UserResponse changeUserRole(@PathVariable Long userId, @RequestBody Roles newRole) {
        return adminService.changeUserRole(userId, newRole);
    }

    @DeleteMapping("/deleteUser/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
    }


    @PostMapping("/banUser/{userId}")
    public UserResponse banUser(@PathVariable Long userId) {
        return adminService.banUser(userId);
    }

    @PostMapping("/createCategory")
    public CategoryResponse createCategory(@RequestBody CategoryRequest request) {
        return adminService.createCategory(request);
    }


    @PutMapping("/updateCategory/{categoryId}")
    public CategoryResponse updateCategory(@PathVariable Long categoryId, @RequestBody CategoryRequest request) {

        return adminService.updateCategory(categoryId, request);
    }

    @DeleteMapping("/deleteCategory/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        adminService.deleteCategory(categoryId);
    }

    @GetMapping("/getAllCategories")
    public List<CategoryResponse> getAllCategories() {
        return adminService.getAllCategories();
    }

    @GetMapping("/PlatformSettings")
    public PlatformSettingsResponse getSettings() {
        return adminService.getSettings();
    }

    @PutMapping("/updateSettings")
    public PlatformSettingsResponse updateSettings(@RequestBody PlatformSettingsRequest request) {
        return adminService.updateSettings(request);
    }
}