package com.unq.dapp0.c1.comprandoencasa.model;

import java.util.ArrayList;

public class DeliveryAtShop extends ShopDelivery {
    private final Turn turn;

    public DeliveryAtShop(Shop shop, ArrayList<Product> products, Customer customer, Turn turn) {
        super(shop, products, customer);
        this.turn = turn;
    }

    public Turn getTurn() {
        return this.turn;
    }
}
