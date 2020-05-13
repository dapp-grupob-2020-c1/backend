package com.unq.dapp0.c1.comprandoencasa.model;

import java.util.ArrayList;

public class DeliveryAtHome extends ShopDelivery {
    private final Location location;

    public DeliveryAtHome(Shop shop, ArrayList<Product> products, Customer customer, Location location) {
        super(shop, products, customer);
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }
}
