package com.unq.dapp0.c1.comprandoencasa.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DiscountByCategory extends Discount {
    private ProductType productType;

    public DiscountByCategory(long id, double percentage, LocalDate startingDate, LocalDate endingDate, Shop shop, ProductType productType) {
        super(id, percentage, startingDate, endingDate, shop);
        this.productType = productType;
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public ArrayList<Product> getProducts() {
        return this.shop.getProducts().stream()
                .filter(p->p.isType(this.productType))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public boolean isTypeCategory() {
        return true;
    }
}
