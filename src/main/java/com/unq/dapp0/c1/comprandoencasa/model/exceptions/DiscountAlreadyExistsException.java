package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Discount;

public class DiscountAlreadyExistsException extends RuntimeException {
    public DiscountAlreadyExistsException(Discount discount) {
        super("The given discount " + discount.getId() + " already exists");
    }
}
