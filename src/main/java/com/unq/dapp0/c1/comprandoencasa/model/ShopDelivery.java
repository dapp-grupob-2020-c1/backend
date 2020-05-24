package com.unq.dapp0.c1.comprandoencasa.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table
public abstract class ShopDelivery {

    @Id
    protected Long id;

    @OneToOne
    private final Shop shop;

    @OneToMany
    private final List<Product> products;

    @OneToOne
    private final Customer customer;


    public ShopDelivery(Shop shop, List<Product> products, Customer customer) {
        this.shop = shop;
        this.products = products;
        this.customer = customer;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Shop getShop(){
        return this.shop;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public Customer getCustomer() {
        return this.customer;
    }
}
