package com.nexusprocure.user.controller;

import com.nexusprocure.user.dto.request.CreateUserRequest;
import com.nexusprocure.user.dto.response.CreateUserResponse;
import com.nexusprocure.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {
private final UserService userService;
@PostMapping
@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request){
    CreateUserResponse response = userService.createUser(request);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
}
}
