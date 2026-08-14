package com.nexusprocure.exception;

public class VendorNotFoundException extends RuntimeException{
    public VendorNotFoundException(Long id){

        super("vendor not found with id: " + id);
    }
}
