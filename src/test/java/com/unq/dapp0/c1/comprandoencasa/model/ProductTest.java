package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.unq.dapp0.c1.comprandoencasa.model.ProductBuilder.aProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ProductTest {

    @Test
    public void aProductCanBeCreatedWithItsAtributes(){

        Product aProduct = new Product("ProductName", "ProductBrand", "ProductImage", new BigDecimal(999));
        assertEquals("ProductName", aProduct.getName());
        assertEquals("ProductBrand", aProduct.getBrand());
        assertEquals("ProductImage", aProduct.getImage());
        assertEquals(new BigDecimal(999), aProduct.getPrice());
    }

    @Test
    public void aProductCanBeOfAType(){
        Product aProduct = aProduct().build();
        ProductType aProductType = mock(ProductType.class);

        aProduct.addType(aProductType);

        assertTrue(aProduct.isType(aProductType));
    }

    @Test
    public void aProductCanBeOfMultipleTypes(){
        Product aProduct = aProduct().build();
        ProductType aProductType = mock(ProductType.class);
        ProductType anotherProductType = mock(ProductType.class);

        aProduct.addType(aProductType);
        aProduct.addType(anotherProductType);

        assertTrue(aProduct.isType(aProductType));
        assertTrue(aProduct.isType(anotherProductType));
    }

    @Test
    public void aProductCanRemoveAType(){
        Product aProduct = aProduct().build();
        ProductType aProductType = mock(ProductType.class);

        aProduct.addType(aProductType);
        assertTrue(aProduct.isType(aProductType));

        aProduct.removeType(aProductType);
        assertFalse(aProduct.isType(aProductType));
    }

}

