package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ProductTest {

    @Test
    public void aProductCanBeOfAType(){
        Product aProduct = new Product();
        ProductType aProductType = mock(ProductType.class);

        aProduct.addType(aProductType);

        assertTrue(aProduct.isType(aProductType));
    }

    @Test
    public void aProductCanBeOfMultipleTypes(){
        Product aProduct = new Product();
        ProductType aProductType = mock(ProductType.class);
        ProductType anotherProductType = mock(ProductType.class);

        aProduct.addType(aProductType);
        aProduct.addType(anotherProductType);

        assertTrue(aProduct.isType(aProductType));
        assertTrue(aProduct.isType(anotherProductType));
    }

    @Test
    public void aProductCanRemoveAType(){
        Product aProduct = new Product();
        ProductType aProductType = mock(ProductType.class);

        aProduct.addType(aProductType);
        assertTrue(aProduct.isType(aProductType));

        aProduct.removeType(aProductType);
        assertFalse(aProduct.isType(aProductType));
    }
}