package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductSmallDTO {
    public Long shopId;
    public Long id;
    public String name;
    public String image;
    public BigDecimal price;
    public Collection<ProductType> types;

    public ProductSmallDTO(){}

    public ProductSmallDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.types = product.getTypes();
        this.shopId = product.getShop().getId();
    }

    public static List<ProductSmallDTO> createProducts(List<Product> products) {
        List<ProductSmallDTO> productSmallDTOList = new ArrayList<>();
        for (Product product : products){
            productSmallDTOList.add(new ProductSmallDTO(product));
        }
        return productSmallDTOList;
    }
}
