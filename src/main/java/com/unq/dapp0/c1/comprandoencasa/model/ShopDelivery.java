package com.unq.dapp0.c1.comprandoencasa.model;

import java.util.ArrayList;

public abstract class ShopDelivery {
    private final Shop shop;
    private final ArrayList<Product> products;
    private final Customer customer;

    public ShopDelivery(Shop shop, ArrayList<Product> products, Customer customer) {
        this.shop = shop;
        this.products = products;
        this.customer = customer;
    }

    public Shop getShop(){
        return this.shop;
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public Customer getCustomer() {
        return this.customer;
    }
}
