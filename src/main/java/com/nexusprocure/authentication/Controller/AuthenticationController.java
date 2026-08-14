package com.nexusprocure.authentication.Controller;

import com.nexusprocure.authentication.Service.AuthenticationService;
import com.nexusprocure.authentication.dto.LoginRequest;
import com.nexusprocure.authentication.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        System.out.println("===== LOGIN CONTROLLER CALLED =====");

        LoginResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }
}
