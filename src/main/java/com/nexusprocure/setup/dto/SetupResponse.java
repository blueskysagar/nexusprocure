package com.nexusprocure.setup.dto;

import lombok.Getter;

@Getter
public class SetupResponse {
    private final String message;
    public SetupResponse(String message){
        this.message = message;
    }

}
