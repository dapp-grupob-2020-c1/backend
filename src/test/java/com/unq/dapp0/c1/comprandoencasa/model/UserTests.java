package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.LocationAlreadyPresentException;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ProductType;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTests {

    @Test
    public void aCustomerCanCreateItsAccountWithANameAndAPasswordThatCanBeValidated(){
        String email = "testo@test.com";
        String password = "1234";

        User user = UserBuilder.anyUser()
                .withEmail(email)
                .withPassword(password)
                .build();

        assertDoesNotThrow(()->user.validate(password, email));

        Throwable exception = assertThrows(InvalidUserException.class,
                ()->user.validate("5678", "anything@any.com"));

        assertEquals("The user, email or password are incorrect", exception.getMessage());
    }

    @Test
    public void aCustomerCannotCreateAnAccountWithAnInvalidEmail(){
        String email = "wrongformat";

        Throwable exceptionFormat1 = assertThrows(InvalidEmailFormatException.class,
                ()-> UserBuilder.anyUser().withEmail(email).build());

        assertEquals("The email format is invalid", exceptionFormat1.getMessage());

        String anotherEmail = "stillawrong@format";

        Throwable exceptionFormat2 = assertThrows(InvalidEmailFormatException.class,
                ()-> UserBuilder.anyUser().withEmail(anotherEmail).build());

        assertEquals("The email format is invalid", exceptionFormat2.getMessage());

        String evenAnotherEmail = "stillawrong.format";

        Throwable exceptionFormat3 = assertThrows(InvalidEmailFormatException.class,
                ()-> UserBuilder.anyUser().withEmail(evenAnotherEmail).build());

        assertEquals("The email format is invalid", exceptionFormat3.getMessage());

        String anotherWrongEmail = "@stillawrong.format";

        Throwable exceptionFormat4 = assertThrows(InvalidEmailFormatException.class,
                ()-> UserBuilder.anyUser().withEmail(anotherWrongEmail).build());

        assertEquals("The email format is invalid", exceptionFormat4.getMessage());
    }


    @Test
    public void aUserCanValidateIfItsTheSameUserAsTheOneGiven(){
        String name = "Real";
        String email = "current@email.com";
        String password = "5678";

        User user = UserBuilder.anyUser()
                .withName(name)
                .withEmail(email)
                .withPassword(password)
                .build();

        assertDoesNotThrow(()->user.validate(user));

        User anotherUser = UserBuilder.anyUser().build();

        Throwable exception = assertThrows(InvalidUserException.class, ()->
                user.validate(anotherUser));

        assertEquals("The user, email or password are incorrect", exception.getMessage());
    }

    @Test
    public void aCustomerHasLocationsAndCanAddMultipleLocations(){
        User user = UserBuilder.anyUser().build();

        assertTrue(user.getLocations().isEmpty());

        Location location1 = mock(Location.class);
        when(location1.getId()).thenReturn(1L);
        user.addLocation(location1);

        assertEquals(1, user.getLocations().size());
        assertTrue(user.getLocations().contains(location1));

        Location location2 = mock(Location.class);
        when(location2.getId()).thenReturn(2L);
        user.addLocation(location2);

        assertEquals(2, user.getLocations().size());
        assertTrue(user.getLocations().contains(location2));
    }

    @Test
    public void aCustomerCanRemoveALocationFromItsList(){
        User user = UserBuilder.anyUser().build();

        assertTrue(user.getLocations().isEmpty());

        Location location1 = mock(Location.class);
        when(location1.getId()).thenReturn(1L);
        user.addLocation(location1);

        assertEquals(1, user.getLocations().size());
        assertTrue(user.getLocations().contains(location1));

        user.removeLocation(location1);

        assertTrue(user.getLocations().isEmpty());
    }

    @Test
    public void aCustomerCannotAddALocationThatItAlreadyContains(){
        User user = UserBuilder.anyUser().build();

        Location location1 = mock(Location.class);
        when(location1.getId()).thenReturn(1L);
        user.addLocation(location1);

        Throwable exception = assertThrows(LocationAlreadyPresentException.class,
                ()->user.addLocation(location1));

        assertEquals("Location "+location1.getId()+" already exists",
                exception.getMessage());
    }

    @Test
    public void aCustomerCanAddAnActiveShoppingList(){
        User user = UserBuilder.anyUser().build();

        ShoppingList shoppingList = mock(ShoppingList.class);

        user.setActiveShoppingList(shoppingList);

        assertEquals(shoppingList, user.getActiveShoppingList());
    }

    @Test
    public void aCustomerCanAddHistoricShoppingLists(){
        User user = UserBuilder.anyUser().build();

        ShoppingList shoppingList1 = mock(ShoppingList.class);

        user.addHistoricShoppingList(shoppingList1);

        assertTrue(user.getHistoricShoppingLists().contains(shoppingList1));
    }

    @Test
    public void aCustomerCanSetProductTypeThresholdsAndTotalPurchaseThresholds(){
        User user = UserBuilder.anyUser().build();

        user.setTotalThreshold(BigDecimal.valueOf(200));

        assertEquals(BigDecimal.valueOf(200), user.getTotalThreshold());

        Map<ProductType, BigDecimal> typeList = new Hashtable<>();

        for (ProductType productType : ProductType.values()){
            typeList.put(productType, BigDecimal.valueOf(1));
        }

        user.setTypesThreshold(typeList);

        assertEquals(typeList, user.getTypesThreshold());
    }

    @Test
    public void aCustomerCanReturnTheHistoricTypesMedianThreshold(){
        User user = UserBuilder.anyUser().build();

        ShoppingList shoppingList1 = mock(ShoppingList.class);
        ShoppingList shoppingList2 = mock(ShoppingList.class);

        user.addHistoricShoppingList(shoppingList1);
        user.addHistoricShoppingList(shoppingList2);

        when(shoppingList1.evaluateTotalFor(any())).thenReturn(BigDecimal.valueOf(0));
        when(shoppingList2.evaluateTotalFor(any())).thenReturn(BigDecimal.valueOf(0));

        when(shoppingList1.evaluateTotalFor(ProductType.Bazaar)).thenReturn(BigDecimal.valueOf(4));
        when(shoppingList2.evaluateTotalFor(ProductType.Bazaar)).thenReturn(BigDecimal.valueOf(6));

        assertEquals(BigDecimal.valueOf(5), user.evaluateHistoricTypeThresholds().get(ProductType.Bazaar));
    }
}
