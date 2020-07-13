package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.Discount;

public class DiscountArgumentsMismatchException extends RuntimeException {
    public DiscountArgumentsMismatchException(Discount discount) {
        super("Discount " + discount.getId() + " does not match the given argument for modification.");
    }
}
