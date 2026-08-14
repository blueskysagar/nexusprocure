package com.nexusprocure.setup.controller;

import com.nexusprocure.setup.dto.SetupAdminRequest;
import com.nexusprocure.setup.dto.SetupResponse;
import com.nexusprocure.setup.service.SetupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setup")
public class SetupController {
    private final SetupService setupService;
    public SetupController(SetupService setupService){
        this.setupService = setupService;
    }
    @PostMapping("/admin")
    public ResponseEntity<SetupResponse> createAdmin(@Valid @RequestBody SetupAdminRequest request){
        SetupResponse response = setupService.createAdmin(request);
        return ResponseEntity.ok(response);
    }

}
