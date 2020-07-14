package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

public class DiscountDoesntExistException extends RuntimeException {
    public DiscountDoesntExistException(Long discountId) {
        super("The Discount with id " + discountId + " does not exist.");
    }
}
