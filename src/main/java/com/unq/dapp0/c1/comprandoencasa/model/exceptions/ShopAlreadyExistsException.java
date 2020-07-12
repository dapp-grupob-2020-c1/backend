package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.Shop;

public class ShopAlreadyExistsException extends RuntimeException {
    public ShopAlreadyExistsException(Shop shop) {
        super("The shop " + shop.getName() + " with id " + shop.getId() + " is already present!");
    }
}
