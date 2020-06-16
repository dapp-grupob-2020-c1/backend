package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

public class CustomerDoesntExistException extends RuntimeException {
    public CustomerDoesntExistException(Long id) {
        super("Customer with id " + id + " does not exist");
    }
}
