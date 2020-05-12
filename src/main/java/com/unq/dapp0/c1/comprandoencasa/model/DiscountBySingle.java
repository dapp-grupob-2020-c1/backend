package com.unq.dapp0.c1.comprandoencasa.model;

import java.time.LocalDate;

public class DiscountBySingle extends Discount {
    private Product product;

    public DiscountBySingle(long id, double percentage, LocalDate startingDate, LocalDate endingDate, Shop shop, Product product) {
        super(id, percentage, startingDate, endingDate, shop);
        this.product = product;
    }

    @Override
    public boolean isTypeSingle() {
        return true;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product){
        this.product = product;
    }

    @Override
    public int compare(Discount discount) {
        if (discount.isTypeMultiple() || this.percentage < discount.percentage){
            return -1;
        } else if (discount.isTypeCategory() || this.percentage > discount.percentage){
            return 1;
        } else {
            return 0;
        }
    }
}
