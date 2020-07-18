package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Discount;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;

public class ProductIsInvalidForDiscount extends RuntimeException {
    public ProductIsInvalidForDiscount(Product product, Discount discount) {
        super("The product " + product.getId() + " is invalid for calculation with discount " + discount.getId());
    }
}
