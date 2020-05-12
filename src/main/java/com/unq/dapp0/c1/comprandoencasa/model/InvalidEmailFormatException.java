package com.unq.dapp0.c1.comprandoencasa.model;

public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException(){
        super("The email format is invalid");
    }
}
