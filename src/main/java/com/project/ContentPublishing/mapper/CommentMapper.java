package com.project.ContentPublishing.mapper;

import com.project.ContentPublishing.dto.Request.CommentRequest;
import com.project.ContentPublishing.dto.Response.CommentResponse;
import com.project.ContentPublishing.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.userName", target = "authorName")
    @Mapping(source = "article.id", target = "articleId")
    @Mapping(source = "article.title", target = "articleTitle")
    CommentResponse toDto(Comment comment);
}