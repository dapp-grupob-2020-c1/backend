package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerTests {

    @Test
    public void aCustomerCanCreateItsAccountWithANameAndAPasswordThatCanBeValidated(){
        String name = "Testeo";
        String email = "testo@test.com";
        String password = "1234";

        Customer customer = CustomerBuilder.anyCustomer()
                .withName(name)
                .withEmail(email)
                .withPassword(password)
                .build();

        assertDoesNotThrow(()->customer.validate(name, password, email));

        Throwable exception = assertThrows(InvalidUserException.class,
                ()->customer.validate("Algo", "5678", "anything@any.com"));

        assertEquals("The user, email or password are incorrect", exception.getMessage());
    }

    @Test
    public void aCustomerCannotCreateAnAccountWithAnInvalidEmail(){
        String email = "wrongformat";

        Throwable exceptionFormat1 = assertThrows(InvalidEmailFormatException.class,
                ()->CustomerBuilder.anyCustomer().withEmail(email).build());

        assertEquals("The email format is invalid", exceptionFormat1.getMessage());

        String anotherEmail = "stillawrong@format";

        Throwable exceptionFormat2 = assertThrows(InvalidEmailFormatException.class,
                ()->CustomerBuilder.anyCustomer().withEmail(anotherEmail).build());

        assertEquals("The email format is invalid", exceptionFormat2.getMessage());

        String evenAnotherEmail = "stillawrong.format";

        Throwable exceptionFormat3 = assertThrows(InvalidEmailFormatException.class,
                ()->CustomerBuilder.anyCustomer().withEmail(evenAnotherEmail).build());

        assertEquals("The email format is invalid", exceptionFormat3.getMessage());

        String anotherWrongEmail = "@stillawrong.format";

        Throwable exceptionFormat4 = assertThrows(InvalidEmailFormatException.class,
                ()->CustomerBuilder.anyCustomer().withEmail(anotherWrongEmail).build());

        assertEquals("The email format is invalid", exceptionFormat4.getMessage());
    }

    @Test
    public void aCustomerHasLocationsAndCanAddMultipleLocations(){
        Customer customer = CustomerBuilder.anyCustomer().build();

        assertTrue(customer.getLocations().isEmpty());

        Location location1 = mock(Location.class);
        when(location1.getID()).thenReturn(1L);
        customer.addLocation(location1);

        assertEquals(1, customer.getLocations().size());
        assertTrue(customer.getLocations().contains(location1));

        Location location2 = mock(Location.class);
        when(location2.getID()).thenReturn(2L);
        customer.addLocation(location2);

        assertEquals(2, customer.getLocations().size());
        assertTrue(customer.getLocations().contains(location2));
    }

    @Test
    public void aCustomerCanRemoveALocationFromItsList(){
        Customer customer = CustomerBuilder.anyCustomer().build();

        assertTrue(customer.getLocations().isEmpty());

        Location location1 = mock(Location.class);
        when(location1.getID()).thenReturn(1L);
        customer.addLocation(location1);

        assertEquals(1, customer.getLocations().size());
        assertTrue(customer.getLocations().contains(location1));

        customer.removeLocation(location1);

        assertTrue(customer.getLocations().isEmpty());
    }

    @Test
    public void aCustomerCannotAddALocationThatItAlreadyContains(){
        Customer customer = CustomerBuilder.anyCustomer().build();

        Location location1 = mock(Location.class);
        when(location1.getID()).thenReturn(1L);
        customer.addLocation(location1);

        Throwable exception = assertThrows(LocationAlreadyPresentException.class,
                ()->customer.addLocation(location1));

        assertEquals("Location "+location1.getID()+" already exists",
                exception.getMessage());
    }

    @Test
    public void aCustomerCanAddAnActiveShoppingList(){
        Customer customer = CustomerBuilder.anyCustomer().build();

        ShoppingList shoppingList = mock(ShoppingList.class);

        customer.addActiveShoppingList(shoppingList);

        assertEquals(shoppingList, customer.getActiveShoppingList());
    }

    @Test
    public void aCustomerCanAddHistoricShoppingLists(){
        Customer customer = CustomerBuilder.anyCustomer().build();

        ShoppingList shoppingList1 = mock(ShoppingList.class);

        customer.addHistoricShoppingList(shoppingList1);

        assertTrue(customer.getHistoricShoppingLists().contains(shoppingList1));
    }

    @Test
    public void aCustomerCanSetProductTypeThresholdsAndTotalPurchaseThresholds(){
        Customer customer = CustomerBuilder.anyCustomer().build();

        customer.setTotalThreshold(BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(200), customer.getTotalThreshold());

        Dictionary<ProductType, BigDecimal> typeList = new Hashtable<>();

        for (ProductType productType : ProductType.values()){
            typeList.put(productType, BigDecimal.valueOf(1));
        }

        customer.setTypesThreshold(typeList);

        assertEquals(typeList, customer.getTypesThreshold());
    }

    @Test
    public void aCustomerCanReturnTheHistoricTypesMedianThreshold(){
        Customer customer = CustomerBuilder.anyCustomer().build();

        ShoppingList shoppingList1 = mock(ShoppingList.class);
        ShoppingList shoppingList2 = mock(ShoppingList.class);

        customer.addHistoricShoppingList(shoppingList1);
        customer.addHistoricShoppingList(shoppingList2);

        when(shoppingList1.evaluateTotalFor(any())).thenReturn(BigDecimal.valueOf(0));
        when(shoppingList2.evaluateTotalFor(any())).thenReturn(BigDecimal.valueOf(0));

        when(shoppingList1.evaluateTotalFor(ProductType.Bazaar)).thenReturn(BigDecimal.valueOf(4));
        when(shoppingList2.evaluateTotalFor(ProductType.Bazaar)).thenReturn(BigDecimal.valueOf(6));

        assertEquals(BigDecimal.valueOf(5), customer.evaluateHistoricTypeThresholds().get(ProductType.Bazaar));
    }
}

class CustomerBuilder{
    private String name;
    private String password;
    private String email;

    public static CustomerBuilder anyCustomer(){
        return new CustomerBuilder();
    }

    private CustomerBuilder(){
        this.name = "Test";
        this.email = "example@example.com";
        this.password = "1111";
    }

    public Customer build() {
        return new Customer(name, password, email);
    }

    public CustomerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CustomerBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public CustomerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
}