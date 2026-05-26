package com.project.ContentPublishing.mapper;

import com.project.ContentPublishing.dto.Request.CategoryRequest;
import com.project.ContentPublishing.dto.Response.CategoryResponse;
import jdk.jfr.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toDto(Category category);
    Category toEntity(CategoryRequest request);
}

