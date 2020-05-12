package com.unq.dapp0.c1.comprandoencasa.model;

import java.time.LocalDate;
import java.util.ArrayList;
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
        if (!this.findProduct(product).isPresent()){
            this.products.add(product);
        } else {
            throw new ProductAlreadyPresentException(product);
        }
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
        return this.products.stream().filter(p-> p.getID().equals(product.getID())).findFirst();
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
}
