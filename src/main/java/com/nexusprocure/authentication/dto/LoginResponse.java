package com.nexusprocure.authentication.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter

public class LoginResponse {
    private final String accessToken;
    private final String tokenType;
    public LoginResponse(String accessToken, String tokenType){
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }


}
