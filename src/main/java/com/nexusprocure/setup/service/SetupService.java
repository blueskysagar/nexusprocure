package com.nexusprocure.setup.service;

import com.nexusprocure.setup.dto.SetupAdminRequest;
import com.nexusprocure.setup.dto.SetupResponse;
import com.nexusprocure.user.entity.Role;
import com.nexusprocure.user.entity.User;
import com.nexusprocure.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SetupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public SetupService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public SetupResponse createAdmin(SetupAdminRequest request){
        if(userRepository.count() > 1){
            throw new RuntimeException("System already initialized");
        }
        User admin = new User();
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        userRepository.save(admin);
        return new SetupResponse("Initial administrator created successfully");
    }
}
