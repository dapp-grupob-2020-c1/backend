package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.unq.dapp0.c1.comprandoencasa.model.ShoppingListBuilder.anyShoppingList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShoppingListTest {

    @Test
    public void aShoppingListStartsEmpty(){
        ShoppingList aShoppingList = anyShoppingList().build();
        assertEquals(0, aShoppingList.countProducts());
        assertEquals(0, aShoppingList.countItems());
    }

    @Test
    public void aShoppingListHasAnEntriesList(){
        ShoppingList aShoppingList = anyShoppingList().build();
        List<ShoppingListEntry> emptyEntriesList = new ArrayList<>();

        assertEquals(emptyEntriesList, aShoppingList.getEntriesList());
    }

    @Test
    public void aShoppingListCanCountProducts(){
        ShoppingList aShoppingList = anyShoppingList().build();
        Product aProduct = mock(Product.class);

        assertEquals(0, aShoppingList.countProducts());
        aShoppingList.addProduct(aProduct, 5);
        assertEquals(1, aShoppingList.countProducts());
    }

    @Test
    public void aShoppingListCanCountItems(){
        ShoppingList aShoppingList = anyShoppingList().build();
        Product aProduct = mock(Product.class);

        assertEquals(0, aShoppingList.countItems());
        aShoppingList.addProduct(aProduct, 5);
        assertEquals(5, aShoppingList.countItems());
    }

    @Test
    public void aShoppingListHasTotalValue(){
        ShoppingList aShoppingList = anyShoppingList().build();
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
        aShoppingList.addProduct(aProduct, 2);
        assertEquals(new BigDecimal(120), aShoppingList.totalValue());
    }

    @Test
    public void aShoppingListHasALocation(){
        Location aLocation = mock(Location.class);
        ShoppingList aShoppingList = anyShoppingList().withLocation(aLocation).build();

        assertEquals(aLocation, aShoppingList.getDeliveryLocation());
    }

    @Test
    public void aShoppingListCanChangeItsLocation(){
        Location aLocation = mock(Location.class);
        Location anotherLocation = mock(Location.class);

        ShoppingList aShoppingList = anyShoppingList().withLocation(aLocation).build();
        assertEquals(aLocation, aShoppingList.getDeliveryLocation());

        aShoppingList.setDeliveryLocation(anotherLocation);
        assertEquals(anotherLocation, aShoppingList.getDeliveryLocation());
    }

    @Test
    public void aShoppingListKnowsItBelongsToAUser(){
        User user = mock(User.class);

        ShoppingList shoppingList = anyShoppingList()
                .withUser(user)
                .build();

        assertEquals(user, shoppingList.getUser());
    }

    @Test
    public void aShoppingListCanEvaluateTheTotalValueForAGivenProductType(){
        ShoppingList shoppingList = anyShoppingList().build();

        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(product1.getId()).thenReturn(1L);
        when(product2.getId()).thenReturn(2L);

        when(product1.isType(ProductType.Bazaar)).thenReturn(true);
        when(product2.isType(ProductType.Bazaar)).thenReturn(true);

        when(product1.getPrice()).thenReturn(BigDecimal.valueOf(1));
        when(product2.getPrice()).thenReturn(BigDecimal.valueOf(1));

        shoppingList.addProduct(product1, 1);
        shoppingList.addProduct(product2, 2);

        assertEquals(BigDecimal.valueOf(3), shoppingList.evaluateTotalFor(ProductType.Bazaar));
    }

}

class ShoppingListBuilder {
    private Location location;
    private User user;

    public static ShoppingListBuilder anyShoppingList(){
        return new ShoppingListBuilder();
    }

    public ShoppingListBuilder() {
        this.location = mock(Location.class);
        this.user = mock(User.class);
    }

    public ShoppingList build() {
        return new ShoppingList(this.location, this.user);
    }

    public ShoppingListBuilder withLocation(Location location){
        this.location = location;
        return this;
    }

    public ShoppingListBuilder withUser(User user) {
        this.user = user;
        return this;
    }
}