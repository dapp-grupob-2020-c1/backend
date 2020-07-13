package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductSmallDTO {
    @NotNull
    public Long shopId;
    public Long id;
    @NotNull
    public String name;
    @NotNull
    public String brand;
    @NotNull
    public String image;
    @NotNull
    public BigDecimal price;
    @NotNull
    public Collection<ProductType> types;

    public ProductSmallDTO(){}

    public ProductSmallDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
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

    public static List<Long> createProductsById(List<Product> products) {
        List<Long> returnList = new ArrayList<>();
        for (Product product : products){
            returnList.add(product.getId());
        }
        return returnList;
    }
}
