package com.project.ContentPublishing.mapper;

import com.project.ContentPublishing.dto.Request.UserRequest;
import com.project.ContentPublishing.dto.Response.UserResponse;
import com.project.ContentPublishing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest dto);

    @Mapping(source = "role", target = "role")
    UserResponse toDto(User user);
}