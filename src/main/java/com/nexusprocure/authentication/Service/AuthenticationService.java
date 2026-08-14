package com.nexusprocure.authentication.Service;

import com.nexusprocure.authentication.dto.LoginRequest;
import com.nexusprocure.authentication.dto.LoginResponse;
import com.nexusprocure.user.entity.Role;
import com.nexusprocure.user.entity.User;
import com.nexusprocure.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    public AuthenticationService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    public LoginResponse login(LoginRequest request) {
        System.out.println("Email received: " + request.getEmail());
        // Ask SpringSecurity to authenticate the user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        //If successful authentication get user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        // Step 3: Generate the JWT token
        String token = jwtService.generateToken(user);
        // Step 4: Return the token
        return new LoginResponse(token,"Bearer");

    }



}
