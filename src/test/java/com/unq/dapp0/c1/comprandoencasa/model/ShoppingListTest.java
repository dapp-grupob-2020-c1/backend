package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.unq.dapp0.c1.comprandoencasa.model.ProductBuilder.aProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShoppingListTest {

    @Test
    public void aShoppingListStartsEmpty(){
        ShoppingList aShoppingList = new ShoppingList();
        assertEquals(0, aShoppingList.countProducts());
    }

    @Test
    public void aShoppingListCanCountProducts(){
        ShoppingList aShoppingList = new ShoppingList();
        Product aProduct = mock(Product.class);

        assertEquals(0, aShoppingList.countProducts());
        aShoppingList.add(aProduct, 5);
        assertEquals(1, aShoppingList.countProducts());
    }

    @Test
    public void aShoppingListCanCountItems(){
        ShoppingList aShoppingList = new ShoppingList();
        Product aProduct = mock(Product.class);

        assertEquals(0, aShoppingList.countItems());
        aShoppingList.add(aProduct, 5);
        assertEquals(5, aShoppingList.countItems());
    }

    @Test
    public void aShoppingListHasTotalValue(){
        ShoppingList aShoppingList = new ShoppingList();
        Product aProduct = mock(Product.class);
        when(aProduct.getPrice()).thenReturn(new BigDecimal(100));

        assertEquals(new BigDecimal(0), aShoppingList.totalValue());
        aShoppingList.add(aProduct, 2);
        assertEquals(new BigDecimal(200), aShoppingList.totalValue());
    }
}
