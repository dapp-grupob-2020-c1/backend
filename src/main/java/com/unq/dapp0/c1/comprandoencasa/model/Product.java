package com.unq.dapp0.c1.comprandoencasa.model;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String brand;

    @Column
    private String image;

    @Column
    private BigDecimal price;

    @ElementCollection
    private Collection<ProductType> types;

    @ManyToOne
    private Shop shop;

    public Product() {}

    public Product(String name, String brand, String image, BigDecimal price, Shop shop) {
        this.name = name;
        this.brand = brand;
        this.image = image;
        this.price = price;
        this.types = new ArrayList<>();
        this.shop = shop;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getImage() {
        return this.image;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Shop getShop() {
        return this.shop;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void addType(ProductType aProductType) {
        this.types.add(aProductType);
    }

    public boolean isType(ProductType aProductType) {
        return this.types.contains(aProductType);
    }

    public void removeType(ProductType aProductType) {
        this.types.remove(aProductType);
    }

}
