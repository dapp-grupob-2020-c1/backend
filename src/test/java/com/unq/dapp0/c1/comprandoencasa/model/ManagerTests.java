package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

public class ManagerTests {

    @Test
    public void aManagerCanBeCreatedWithANameAndAPasswordThatItCanThenValidate(){
        String name = "Test";
        String password = "1234";

        Manager manager = ManagerBuilder.anyManager()
                .withName(name)
                .withPassword(password)
                .build();

        assertDoesNotThrow(()->manager.validate(name, password));

        Throwable exception = assertThrows(InvalidManagerException.class, ()->
                manager.validate("Otro", "5678"));

        assertEquals("Invalid manager access", exception.getMessage());
    }

    @Test
    public void aManagerCanValidateIfItsTheSameManagerAsTheOneGiven(){
        String name = "Real";
        String password = "5678";

        Manager manager = ManagerBuilder.anyManager()
                .withName(name)
                .withPassword(password)
                .build();

        assertDoesNotThrow(()->manager.validate(manager));

        Manager anotherManager = ManagerBuilder.anyManager().build();

        Throwable exception = assertThrows(InvalidManagerException.class, ()->
                manager.validate(anotherManager));

        assertEquals("Invalid manager access", exception.getMessage());
    }

    @Test
    public void aManagerCanInitializeWithAShopAndReturnIt(){
        Shop shop = mock(Shop.class);

        Manager manager = ManagerBuilder.anyManager()
                .withShop(shop)
                .build();

        assertEquals(shop, manager.getShop());
    }

}

class ManagerBuilder{
    private String name;
    private String password;
    private Shop shop;

    public static ManagerBuilder anyManager(){
        return new ManagerBuilder();
    }

    private ManagerBuilder(){
        this.name = "Test";
        this.password = "1234";
        this.shop = mock(Shop.class);
    }

    public ManagerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ManagerBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public ManagerBuilder withShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public Manager build() {
        return new Manager(name, password, shop);
    }
}
