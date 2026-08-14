package com.nexusprocure.user.mapper;

import com.nexusprocure.user.dto.request.CreateUserRequest;
import com.nexusprocure.user.dto.response.CreateUserResponse;
import com.nexusprocure.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(CreateUserRequest request);
    CreateUserResponse toResponse(User user);

}
