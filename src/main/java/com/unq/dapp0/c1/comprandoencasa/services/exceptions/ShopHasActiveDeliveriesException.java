package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;

public class ShopHasActiveDeliveriesException extends RuntimeException {
    public ShopHasActiveDeliveriesException(Shop shop) {
        super("The shop " + shop.getName() + " with id" + shop.getId() + " has active deliveries and cannot be deleted.");
    }
}
