package com.unq.dapp0.c1.comprandoencasa.model;

import java.util.ArrayList;
import java.util.Collection;

public class Product {
    private Collection<ProductType> types;

    Product() {
        types = new ArrayList<ProductType>();
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
}
