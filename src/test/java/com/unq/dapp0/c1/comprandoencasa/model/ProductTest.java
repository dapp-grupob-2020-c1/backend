package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    public void aProductCanBeOfAType(){
        Product prod = new Product();
        ProductType type = new ProductType();
        prod.addType(type);

        assertTrue(prod.isType(type));
    }

    @Test
    public void aProductCanBeOfMultipleTypes(){
        Product prod = new Product();
        ProductType type1 = new ProductType();
        ProductType type2 = new ProductType();

        prod.addType(type1);
        prod.addType(type2);

        assertTrue(prod.isType(type1));
        assertTrue(prod.isType(type2));
    }

    @Test
    public void aProductCanRemoveAType(){
        Product prod = new Product();
        ProductType type = new ProductType();

        prod.addType(type);
        assertTrue(prod.isType(type));

        prod.removeType(type);
        assertFalse(prod.isType(type));
    }
}