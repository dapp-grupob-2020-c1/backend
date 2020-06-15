package com.unq.dapp0.c1.comprandoencasa.webservices.exceptions;

public class ShopDoesntExistException extends RuntimeException {
    public ShopDoesntExistException(Long id) {
        super("Shop with id " + id + " does not exist");
    }
}
