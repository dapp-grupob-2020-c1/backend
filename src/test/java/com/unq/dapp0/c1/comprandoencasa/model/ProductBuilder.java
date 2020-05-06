package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;

public class ProductBuilder {
    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }
    private String name = "no name";
    private String brand = "no brand";
    private BigDecimal price = new BigDecimal(999);

    public Product build() {
        return new Product(name, brand, price);
    }
}
