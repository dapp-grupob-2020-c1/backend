package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomerTests {

    @Test
    public void aCustomerCanCreateItsAccountWithANameAndAPasswordThatCanBeValidated(){
        String name = "Testo";
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