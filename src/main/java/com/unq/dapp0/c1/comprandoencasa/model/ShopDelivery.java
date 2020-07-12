package com.unq.dapp0.c1.comprandoencasa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ShopDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected long id;
    @OneToOne
    protected Shop shop;
    @OneToMany
    protected List<Product> products;
    @OneToOne
    protected User user;

    public ShopDelivery(){}

    public ShopDelivery(Shop shop, List<Product> products, User user) {
        this.shop = shop;
        this.products = products;
        this.user = user;
    }

    public Shop getShop() {
        return this.shop;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public User getUser() {
        return this.user;
    }
}
