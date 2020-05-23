package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidManagerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

public class ManagerTests {

    @Test
    public void aManagerCanBeCreatedWithANameAndAPasswordThatItCanThenValidate(){
        String name = "Testo";
        String email = "test@testo.com";
        String password = "1234";

        Manager manager = ManagerBuilder.anyManager()
                .withName(name)
                .withEmail(email)
                .withPassword(password)
                .build();

        assertDoesNotThrow(()->manager.validate(name, password, email));

        Throwable exception = assertThrows(InvalidManagerException.class, ()->
                manager.validate("Otro", "5678", "wrongful@another.com"));

        assertEquals("Invalid manager access", exception.getMessage());
    }

    @Test
    public void aManagerCannotBeCreatedWithAnInvalidEmail(){
        String email = "wrongformat";

        Throwable exceptionFormat1 = assertThrows(InvalidEmailFormatException.class,
                ()->ManagerBuilder.anyManager().withEmail(email).build());

        assertEquals("The email format is invalid", exceptionFormat1.getMessage());

        String anotherEmail = "stillawrong@format";

        Throwable exceptionFormat2 = assertThrows(InvalidEmailFormatException.class,
                ()->ManagerBuilder.anyManager().withEmail(anotherEmail).build());

        assertEquals("The email format is invalid", exceptionFormat2.getMessage());

        String evenAnotherEmail = "stillawrong.format";

        Throwable exceptionFormat3 = assertThrows(InvalidEmailFormatException.class,
                ()->ManagerBuilder.anyManager().withEmail(evenAnotherEmail).build());

        assertEquals("The email format is invalid", exceptionFormat3.getMessage());

        String anotherWrongEmail = "@stillawrong.format";

        Throwable exceptionFormat4 = assertThrows(InvalidEmailFormatException.class,
                ()->ManagerBuilder.anyManager().withEmail(anotherWrongEmail).build());

        assertEquals("The email format is invalid", exceptionFormat4.getMessage());
    }

    @Test
    public void aManagerCanValidateIfItsTheSameManagerAsTheOneGiven(){
        String name = "Real";
        String email = "current@email.com";
        String password = "5678";

        Manager manager = ManagerBuilder.anyManager()
                .withName(name)
                .withEmail(email)
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
    private String email;

    public static ManagerBuilder anyManager(){
        return new ManagerBuilder();
    }

    private ManagerBuilder(){
        this.name = "Test";
        this.password = "1234";
        this.email = "example@example.com";
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

    public ManagerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public Manager build() {
        return new Manager(name, password, email, shop);
    }
}
