package com.project.ContentPublishing.mapper;

import com.project.ContentPublishing.dto.Response.PlatformSettingsResponse;
import com.project.ContentPublishing.model.PlatformSettings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlatformSettingsMapper {
    PlatformSettingsResponse toDto(PlatformSettings settings);
}