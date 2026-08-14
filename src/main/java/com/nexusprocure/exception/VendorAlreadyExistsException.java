package com.nexusprocure.exception;

public class VendorAlreadyExistsException extends RuntimeException{
    public VendorAlreadyExistsException(String message){
        super(message);
    }
}
