package com.project.ContentPublishing.mapper;

import com.project.ContentPublishing.dto.Request.ArticleRequest;
import com.project.ContentPublishing.dto.Response.ArticleResponse;
import com.project.ContentPublishing.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(source = "author.userName", target = "author")
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "commentCount", target = "commentCount")
    ArticleResponse toDto(Article article);
}