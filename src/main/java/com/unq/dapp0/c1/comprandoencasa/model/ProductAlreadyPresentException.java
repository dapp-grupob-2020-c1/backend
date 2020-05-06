package com.unq.dapp0.c1.comprandoencasa.model;

public class ProductAlreadyPresentException extends RuntimeException {
    public ProductAlreadyPresentException(Product product) {
        super("The product "+product.getID()+" is already present in the list");
    }
}
