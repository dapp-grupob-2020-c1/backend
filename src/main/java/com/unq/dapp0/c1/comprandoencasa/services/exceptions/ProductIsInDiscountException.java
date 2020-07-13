package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.Product;

public class ProductIsInDiscountException extends RuntimeException {
    public ProductIsInDiscountException(Product product) {
        super("The product " + product.getName() + " with id " + product.getId() + "is present in one or more Discounts and cannot be deleted.");
    }
}
