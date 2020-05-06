package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTypeTest {

    @Test
    public void aProductTypeCanBeCreatedWithAName(){
        ProductType prodType = new ProductType("Food");
        assertEquals("Food", prodType.getName());
    }

    @Test
    public void aProductTypeNameCanBeChanged(){
        ProductType prodType = new ProductType("Food");
        prodType.setName("Electronics");
        assertEquals("Electronics", prodType.getName());
    }
}