package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShoppingListTest {

    @Test
    public void aShoppingListStartsEmpty(){
        ShoppingList aShoppingList = ShoppingListBuilder.anyShoppingList().build();
        assertEquals(0, aShoppingList.countProducts());
    }

    @Test
    public void aShoppingListHasAnEntriesList(){
        ShoppingList aShoppingList = ShoppingListBuilder.anyShoppingList().build();
        List<Map.Entry<Product, Integer>> emptyEntriesList = new ArrayList<>();

        assertEquals(emptyEntriesList, aShoppingList.getEntries());
    }

    @Test
    public void aShoppingListCanCountProducts(){
        ShoppingList aShoppingList = ShoppingListBuilder.anyShoppingList().build();
        Product aProduct = mock(Product.class);

        assertEquals(0, aShoppingList.countProducts());
        aShoppingList.add(aProduct, 5);
        assertEquals(1, aShoppingList.countProducts());
    }

    @Test
    public void aShoppingListCanCountItems(){
        ShoppingList aShoppingList = ShoppingListBuilder.anyShoppingList().build();
        Product aProduct = mock(Product.class);

        assertEquals(0, aShoppingList.countItems());
        aShoppingList.add(aProduct, 5);
        assertEquals(5, aShoppingList.countItems());
    }

    @Test
    public void aShoppingListHasTotalValue(){
        ShoppingList aShoppingList = ShoppingListBuilder.anyShoppingList().build();
        Product aProduct = mock(Product.class);
        Shop shop = mock(Shop.class);
        Discount discount = mock(Discount.class);

        ArrayList<Discount> discounts = new ArrayList<>();
        discounts.add(discount);

        when(aProduct.getShop()).thenReturn(shop);
        when(aProduct.getPrice()).thenReturn(BigDecimal.valueOf(10));
        when(shop.getActiveDiscounts()).thenReturn(discounts);
        when(discount.compare(discount)).thenReturn(0);
        when(discount.calculateFor(any())).thenReturn(new BigDecimal(100));

        assertEquals(new BigDecimal(0), aShoppingList.totalValue());
        aShoppingList.add(aProduct, 2);
        assertEquals(new BigDecimal(120), aShoppingList.totalValue());
    }

    @Test
    public void aShoppingListHasALocation(){
        Location aLocation = mock(Location.class);
        ShoppingList aShoppingList = ShoppingListBuilder.anyShoppingList().withLocation(aLocation).build();

        assertEquals(aLocation, aShoppingList.getLocation());
    }

    @Test
    public void aShoppingListCanChangeItsLocation(){
        Location aLocation = mock(Location.class);
        Location anotherLocation = mock(Location.class);

        ShoppingList aShoppingList = ShoppingListBuilder.anyShoppingList().withLocation(aLocation).build();
        assertEquals(aLocation, aShoppingList.getLocation());

        aShoppingList.setLocation(anotherLocation);
        assertEquals(anotherLocation, aShoppingList.getLocation());
    }

}

class ShoppingListBuilder {
    private Location location;
    public static ShoppingListBuilder anyShoppingList(){
        return new ShoppingListBuilder();
    }

    public ShoppingListBuilder() {
        this.location = mock(Location.class);
    }
    public ShoppingListBuilder withLocation(Location location){
        this.location = location;
        return this;
    }
    public ShoppingList build() {
        return new ShoppingList(this.location);
    }
}