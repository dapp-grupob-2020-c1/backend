package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("The user, email or password are incorrect");
    }
}
