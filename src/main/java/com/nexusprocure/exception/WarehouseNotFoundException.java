package com.nexusprocure.exception;

public class WarehouseNotFoundException extends RuntimeException{
    public WarehouseNotFoundException(Long id){

        super("warehouse not found with id: " + id);
    }
}
