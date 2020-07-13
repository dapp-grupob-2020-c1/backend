package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.Product;
import com.unq.dapp0.c1.comprandoencasa.model.ProductType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static List<ProductDTO> parseProducts(List<Product> products) {
        List<ProductDTO> response = new ArrayList<>();
        for (Product product : products){
            response.add(new ProductDTO(product));
        }
        return response;
    }
}
