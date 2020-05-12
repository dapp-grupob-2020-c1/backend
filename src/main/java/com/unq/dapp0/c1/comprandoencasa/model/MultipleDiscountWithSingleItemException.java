package com.unq.dapp0.c1.comprandoencasa.model;

public class MultipleDiscountWithSingleItemException extends RuntimeException {
    public MultipleDiscountWithSingleItemException(Product product){
        super("Removal of the product "+product.getID()+" will cause the Discount to be composed of a single item. Use the class DiscountBySingle instead for the item that should remain");
    }
}
