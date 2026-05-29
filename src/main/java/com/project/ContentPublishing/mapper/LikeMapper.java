package com.project.ContentPublishing.mapper;

import com.project.ContentPublishing.dto.Response.LikeResponse;
import com.project.ContentPublishing.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(source = "user.userName", target = "username")
    @Mapping(source = "article.id", target = "articleId")
    @Mapping(source = "article.title", target = "articleTitle")
    LikeResponse toDto(Like like);
}