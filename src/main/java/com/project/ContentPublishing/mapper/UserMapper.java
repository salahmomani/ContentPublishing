package com.project.ContentPublishing.mapper;

import com.project.ContentPublishing.dto.Request.UserRequest;
import com.project.ContentPublishing.dto.Response.UserResponse;
import com.project.ContentPublishing.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest dto);

    UserResponse toDto(User user);
}