package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;

import java.math.BigDecimal;
import java.util.Collection;

public class ProductDTO {
    public Long id;
    public String name;
    public String image;
    public BigDecimal price;
    public Collection<ProductType> types;
    public ShopDTO shop;

    public ProductDTO(){}

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.types = product.getTypes();
        this.shop = new ShopDTO(product.getShop());
    }
}
