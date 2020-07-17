package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

public class NoActiveShoppingListException extends RuntimeException {
    public NoActiveShoppingListException(Long userId) {
        super("There's no active shopping list for the user " + userId);
    }
}
