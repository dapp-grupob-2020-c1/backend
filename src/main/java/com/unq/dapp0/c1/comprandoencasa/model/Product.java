package com.unq.dapp0.c1.comprandoencasa.model;

import java.util.ArrayList;
import java.util.Collection;

public class Product {
    private final String name;
    private final String brand;
    private Collection<ProductType> types;

    public Product(String name, String brand) {
        this.name = name;
        this.brand = brand;
        this.types = new ArrayList<ProductType>();
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
}
