package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.Product;

public class ProductAlreadyPresentException extends RuntimeException {
    public ProductAlreadyPresentException(Product product) {
        super("The product "+product.getId()+" is already present in the list");
    }
}
