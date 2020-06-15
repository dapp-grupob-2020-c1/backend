package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

public class ProductDoesntExistException extends RuntimeException {
    public ProductDoesntExistException(Long id) {
        super("Product with id " + id + " does not exist");
    }
}
