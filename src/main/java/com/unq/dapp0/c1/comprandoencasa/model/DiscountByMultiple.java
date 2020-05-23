package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.MultipleDiscountWithSingleItemException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DiscountByMultiple extends Discount {
    private ArrayList<Product> products;

    public DiscountByMultiple(long id, double percentage, LocalDate startingDate, LocalDate endingDate, Shop shop, ArrayList<Product> products) {
        super(id, percentage, startingDate, endingDate, shop);
        this.products = products;
    }

    @Override
    public boolean isTypeMultiple() {
        return true;
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.findProduct(product).ifPresent(value -> {
            if (this.products.size() == 2){
                throw new MultipleDiscountWithSingleItemException(product);
            } else {
                this.products.remove(value);
            }
        });
    }

    private Optional<Product> findProduct(Product product){
        return this.products.stream().filter(p-> p.getId().equals(product.getId())).findFirst();
    }

    @Override
    public int compare(Discount discount) {
        if (this.percentage < discount.percentage){
            return -1;
        } else if (discount.isTypeSingle() || discount.isTypeCategory() || this.percentage > discount.percentage){
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public BigDecimal calculateFor(List<Map.Entry<Product, Integer>> products) {
        BigDecimal total = new BigDecimal(0);
        boolean isValid = this.products.stream().allMatch(product -> this.mapListContains(product, products));
        if (isValid){
            for (Map.Entry<Product, Integer> productIntegerEntry : products){
                Product product = productIntegerEntry.getKey();
                if (this.findProduct(product).isPresent()){
                    BigDecimal discount = BigDecimal.valueOf(this.percentage).multiply(BigDecimal.valueOf(0.01)).multiply(product.getPrice());
                    total = total.add(product.getPrice().subtract(discount).multiply(BigDecimal.valueOf(productIntegerEntry.getValue())));
                    if (productIntegerEntry.getValue() > 1){
                        productIntegerEntry.setValue(productIntegerEntry.getValue() - 1);
                    } else {
                        products.remove(productIntegerEntry);
                    }
                }
            }
        }
        return total;
    }

    private boolean mapListContains(Product product, List<Map.Entry<Product, Integer>> products) {
        for (Map.Entry<Product, Integer> productIntegerEntry : products){
            Product key = productIntegerEntry.getKey();
            if (key.getId().equals(product.getId())){
                return true;
            }
        }
        return false;
    }
}
