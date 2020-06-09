package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class ProductBuilder {
    private String name = "no name";
    private String brand = "no brand";
    private String image = "no image";
    private BigDecimal price = new BigDecimal(999);
    private Shop shop = mock(Shop.class);
    private List<ProductType> types = new ArrayList<>();

    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }

    public Product build() {
        return new Product(name, brand, image, price, shop, types);
    }

    public ProductBuilder withTypes(List<ProductType> types){
        this.types = types;
        return this;
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withShop(Shop shop) {
        this.shop = shop;
        return this;
    }
}
