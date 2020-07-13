package com.unq.dapp0.c1.comprandoencasa.model.objects;

import javax.persistence.*;

import java.util.List;

@Entity
@DiscriminatorColumn(name="Type")
@Inheritance(strategy=InheritanceType.JOINED)
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

    public Long getId(){return this.id;}

    public void setId(Long id){this.id = id;}

    public Shop getShop() {
        return this.shop;
    }

    public void setShop(Shop shop) {this.shop = shop;}

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products){this.products = products;}

    public User getUser() {
        return this.user;
    }

    public void setUser(User user){this.user = user;}
}
