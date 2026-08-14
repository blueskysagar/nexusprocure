package com.nexusprocure.exception;

public class PurchaseOrderNotFoundException extends RuntimeException{
    public PurchaseOrderNotFoundException(Long id){
        super("Purchase Order Not Found with id: " + id);
    }
}
