package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidManagerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ManagerTests {

    @Test
    public void aManagerCanBeCreatedWithANameAndAPasswordThatItCanThenValidate(){
        String email = "test@testo.com";
        String password = "1234";

        Manager manager = ManagerBuilder.anyManager()
                .withEmail(email)
                .withPassword(password)
                .build();

        assertDoesNotThrow(()->manager.validate(password, email));

        Throwable exception = assertThrows(InvalidManagerException.class, ()->
                manager.validate("5678", "wrongful@another.com"));

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

}
