package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

public class NotAnActiveShoppingListException extends RuntimeException {
    public NotAnActiveShoppingListException(Long userId) {
        super("The user " + userId + " doesn't have an active shopping list. Please, create one first.");
    }
}
