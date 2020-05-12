package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @Override
    public BigDecimal calculateFor(List<Map.Entry<Product, Integer>> products) {
        BigDecimal total = new BigDecimal(0);
        for (Map.Entry<Product, Integer> productIntegerEntry : products){
            Product product = productIntegerEntry.getKey();
            if (product.getID().equals(this.product.getID())){
                BigDecimal discount = BigDecimal.valueOf(this.percentage).multiply(BigDecimal.valueOf(0.01)).multiply(product.getPrice());
                total = total.add(product.getPrice().subtract(discount).multiply(BigDecimal.valueOf(productIntegerEntry.getValue())));
                products.remove(productIntegerEntry);
            }
        }
        return total;
    }
}
