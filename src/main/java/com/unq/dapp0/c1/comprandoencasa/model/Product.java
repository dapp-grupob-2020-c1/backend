package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Product {
    private String name;
    private String brand;
    private String image;
    private BigDecimal price;
    private Collection<ProductType> types;
    private Shop shop;

    public Product(String name, String brand, String image, BigDecimal price, Shop shop) {
        this.name = name;
        this.brand = brand;
        this.image = image;
        this.price = price;
        this.types = new ArrayList<>();
        this.shop = shop;
    }

    /**
     * @return Long representing the unique ID.
     */
    public Long getID() {
        return null;
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

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Shop getShop() {
        return this.shop;
    }

    public BigDecimal getTotalPrice(List<Map.Entry<Product, Integer>> products) {
        // aca se encuentra a si mismo dentro de la shopping list, y calcula su precio
        // teniendo en cuenta los posibles descuentos
        return this.getPrice();
    }
}
