package com.unq.dapp0.c1.comprandoencasa.model;

public class ProductBuilder {
    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }
    private String name = "no name";
    private String brand = "no brand";

    public Product build() {
        return new Product(name, brand);
    }
}
