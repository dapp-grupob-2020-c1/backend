package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class Product {
    private String name;
    private String brand;
    private String image;
    private BigDecimal price;
    private Collection<ProductType> types;

    public Product(String name, String brand, String image, BigDecimal price) {
        this.name = name;
        this.brand = brand;
        this.image = image;
        this.price = price;
        this.types = new ArrayList<>();
    }

    /**
     * @return Long representing the unique ID.
     */
    public Long getID() {
        return null;
    }

    public void addType(ProductType type) {
        this.types.add(type);
    }

    public boolean isType(ProductType productType) {
        return this.types.contains(productType);
    }

    public void removeType(ProductType type) {
        this.types.remove(type);
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

    public void setName(String name) {
        this.name = name;
    }
}
