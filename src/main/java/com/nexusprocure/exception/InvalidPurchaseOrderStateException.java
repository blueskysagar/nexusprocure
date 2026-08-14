package com.nexusprocure.exception;

public class InvalidPurchaseOrderStateException extends RuntimeException{
    public InvalidPurchaseOrderStateException(String message){
        super(message);
    }
}
