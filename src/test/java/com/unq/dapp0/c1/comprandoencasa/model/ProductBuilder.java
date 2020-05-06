package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;

public class ProductBuilder {
    private String name = "no name";
    private String brand = "no brand";
    private BigDecimal price = new BigDecimal(999);

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public Product build() {
        return new Product(name, brand, price);
    }
}
