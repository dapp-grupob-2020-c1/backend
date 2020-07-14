package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;

public class MultipleDiscountWithSingleItemException extends RuntimeException {
    public MultipleDiscountWithSingleItemException(Product product) {
        super("Removal of the product " + product.getId() + " will cause the Discount to be composed of a single item. Use the class DiscountBySingle instead for the item that should remain");
    }
}
