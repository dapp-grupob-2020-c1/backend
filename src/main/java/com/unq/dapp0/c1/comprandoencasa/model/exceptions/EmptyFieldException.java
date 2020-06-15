package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

public class EmptyFieldException extends RuntimeException{
    public EmptyFieldException(String field){
        super("The field " + field + " is empty");
    }
}
