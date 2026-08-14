package com.nexusprocure.user.service;

import com.nexusprocure.user.dto.request.CreateUserRequest;
import com.nexusprocure.user.dto.response.CreateUserResponse;
import com.nexusprocure.user.entity.User;
import com.nexusprocure.user.exception.EmailAlreadyExistsException;
import com.nexusprocure.user.mapper.UserMapper;
import com.nexusprocure.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public CreateUserResponse createUser(CreateUserRequest request)
    {
        //duplicate email checking
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {throw new EmailAlreadyExistsException("Email already exists");
        });

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);


    }

}
