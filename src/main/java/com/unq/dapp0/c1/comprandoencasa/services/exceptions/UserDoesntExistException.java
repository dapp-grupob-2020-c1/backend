package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

public class UserDoesntExistException extends RuntimeException {
    public UserDoesntExistException(Long id) {
        super("Customer with id " + id + " does not exist");
    }
}
