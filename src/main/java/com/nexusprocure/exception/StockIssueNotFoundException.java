package com.nexusprocure.exception;

public class StockIssueNotFoundException extends RuntimeException{
    public StockIssueNotFoundException(Long id){
        super("StockIssue not found with id: " + id);

    }
}
