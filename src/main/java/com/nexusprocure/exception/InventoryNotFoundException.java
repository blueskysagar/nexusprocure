package com.nexusprocure.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(Long id){
        super("Inventory Not Found with id: " + id);
    }
}
