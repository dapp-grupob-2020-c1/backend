package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;

public class ProductBuilder {
    private String name = "no name";
    private String brand = "no brand";
    private String image = "no image";
    private BigDecimal price = new BigDecimal(999);
    private Shop shop = mock(Shop.class);

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public Product build() {
        return new Product(name, brand, image, price, shop);
    }
}
