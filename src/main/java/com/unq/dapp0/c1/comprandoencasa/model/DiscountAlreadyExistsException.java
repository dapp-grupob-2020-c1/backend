package com.unq.dapp0.c1.comprandoencasa.model;

public class DiscountAlreadyExistsException extends RuntimeException {
    public DiscountAlreadyExistsException(Discount discount) {
        super("The given discount "+discount.getID()+" already exists");
    }
}
