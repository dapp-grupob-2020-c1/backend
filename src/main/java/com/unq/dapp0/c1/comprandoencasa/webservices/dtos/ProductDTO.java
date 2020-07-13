package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;

import java.math.BigDecimal;
import java.util.Collection;

public class ProductDTO {
    public Long id;
    public String name;
    public String brand;
    public String image;
    public BigDecimal price;
    public Collection<ProductType> types;
    public ShopSmallDTO shop;

    public ProductDTO(){}

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.types = product.getTypes();
        this.shop = new ShopSmallDTO(product.getShop());
    }
}
