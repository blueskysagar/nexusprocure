package com.nexusprocure.user.dto.response;

import lombok.Getter;
import lombok.Setter;

import com.nexusprocure.user.entity.Role;

@Getter
@Setter

public class CreateUserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}
