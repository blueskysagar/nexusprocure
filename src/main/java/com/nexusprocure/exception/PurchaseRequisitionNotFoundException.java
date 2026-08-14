package com.nexusprocure.exception;

public class PurchaseRequisitionNotFoundException extends RuntimeException{
    public PurchaseRequisitionNotFoundException(Long id){
        super("Purchase Requisition not found with id: " + id);
    }
}
