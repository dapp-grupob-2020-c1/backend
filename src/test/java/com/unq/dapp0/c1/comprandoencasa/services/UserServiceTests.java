package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.User;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void serviceCanCreateANewUserSaveItAndRetrieveIt(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        User customer = userService.createUser(name, email, password);

        User retrievedCustomer = userService.findUserById(customer.getId());

        assertEquals(customer.getId(), retrievedCustomer.getId());
        assertEquals(customer.getName(), retrievedCustomer.getName());
    }

    @Test
    public void serviceThrowsUserDoesntExistWhenTryingToFindByInexistentId(){
        UserDoesntExistException exception = assertThrows(UserDoesntExistException.class, ()-> userService.findUserById(0L));
        assertNotNull(exception);
        assertEquals("User with id 0 does not exist", exception.getMessage());
    }

    @Test
    public void serviceThrowsFieldAlreadyExistsIfTheNameOrEmailAreAlreadyInUse(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        userService.createUser(name, email, password);

        FieldAlreadyExistsException exceptionName = assertThrows(FieldAlreadyExistsException.class, ()-> userService.createUser(name, email, password));
        assertNotNull(exceptionName);
        assertEquals("The field name already exists", exceptionName.getMessage());

        FieldAlreadyExistsException exceptionEmail = assertThrows(FieldAlreadyExistsException.class, ()-> userService.createUser("other", email, password));
        assertNotNull(exceptionEmail);
        assertEquals("The field email already exists", exceptionEmail.getMessage());
    }

    @Test
    public void serviceThrowsInvalidEmailFormatIfTheEmailFormatIsWrongDuringCreation(){
        String name = "foo";
        String email = "foo";
        String password = "1234";

        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, ()-> userService.createUser(name, email, password));
        assertNotNull(exception);
        assertEquals("The email format is invalid", exception.getMessage());
    }

    @Test
    public void serviceThrowsEmptyFieldExceptionIfEitherNameEmailOrPasswordAreEmptyDuringCreation(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        EmptyFieldException emptyNameException = assertThrows(EmptyFieldException.class, ()-> userService.createUser("", email, password));
        assertNotNull(emptyNameException);
        assertEquals("The field name is empty", emptyNameException.getMessage());

        EmptyFieldException emptyEmailException = assertThrows(EmptyFieldException.class, ()-> userService.createUser(name, "", password));
        assertNotNull(emptyEmailException);
        assertEquals("The field email is empty", emptyEmailException.getMessage());

        EmptyFieldException emptyPassException = assertThrows(EmptyFieldException.class, ()-> userService.createUser(name, email, ""));
        assertNotNull(emptyPassException);
        assertEquals("The field password is empty", emptyPassException.getMessage());
    }

    @Test
    public void serviceCanValidateAUserWithAGivenEmailAndPassword() throws Exception {
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        User customer = userService.createUser(name, email, password);

        User retrievedCustomer = userService.validateUser(email, password);

        assertEquals(customer.getId(), retrievedCustomer.getId());
        assertEquals(customer.getName(), retrievedCustomer.getName());
    }

    @Test
    public void serviceThrowsInvalidUserExceptionWhenTryingToValidateWithANonExistantEmailOrWrongPassword(){
        String name = "foo";
        String email1 = "foo@foo.com";
        String email2 = "faa@foo.com";
        String password1 = "1234";
        String password2 = "1111";

        userService.createUser(name, email1, password1);

        InvalidUserException noEmailException = assertThrows(InvalidUserException.class, ()-> userService.validateUser(email2, password1));
        assertNotNull(noEmailException);
        assertEquals("The user, email or password are incorrect", noEmailException.getMessage());

        InvalidUserException badPasswordException = assertThrows(InvalidUserException.class, ()-> userService.validateUser(email1, password2));
        assertNotNull(badPasswordException);
        assertEquals("The user, email or password are incorrect", badPasswordException.getMessage());
    }

    @Test
    public void serviceThrowsInvalidEmailFormatIfTheEmailFormatIsWrongDuringValidation(){
        String email = "foo";
        String password = "1234";

        InvalidEmailFormatException exception = assertThrows(InvalidEmailFormatException.class, ()-> userService.validateUser(email, password));
        assertNotNull(exception);
        assertEquals("The email format is invalid", exception.getMessage());
    }

    @Test
    public void serviceThrowsEmptyFieldExceptionIfEitherEmailOrPasswordAreEmptyDuringValidation(){
        String email = "foo@foo.com";
        String password = "1234";

        EmptyFieldException emptyEmailException = assertThrows(EmptyFieldException.class, ()-> userService.validateUser("", password));
        assertNotNull(emptyEmailException);
        assertEquals("The field email is empty", emptyEmailException.getMessage());

        EmptyFieldException emptyPassException = assertThrows(EmptyFieldException.class, ()-> userService.validateUser(email, ""));
        assertNotNull(emptyPassException);
        assertEquals("The field password is empty", emptyPassException.getMessage());
    }

    @Test
    public void serviceCanAddANewLocationToAUserAndReturnAllLocationsOfAGivenUser(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        User customer = userService.createUser(name, email, password);

        String address = "foo";
        Double latitude = 1.0;
        Double longitude = 1.0;

        Location location = userService.addLocationTo(customer.getId(), address, latitude, longitude);

        List<Location> locationList = userService.getLocationsOf(customer.getId());

        assertEquals(1, locationList.size());
        assertEquals(location.getId(), locationList.get(0).getId());
    }

    @Test
    public void serviceThrowsEmptyFieldExceptionIfTheAddressIsEmptyWhenAddingANewLocationToACustomer(){
        Long id = 1L;

        Double latitude = 1.0;
        Double longitude = 1.0;

        EmptyFieldException emptyAddressException = assertThrows(EmptyFieldException.class, ()-> userService.addLocationTo(id, "", latitude, longitude));
        assertNotNull(emptyAddressException);
        assertEquals("The field address is empty", emptyAddressException.getMessage());
    }

    @Test
    public void serviceThrowsUserDoesntExistExceptionWhenTryingToAddANewLocationWithAnInvalidUserId(){
        Long id = 2L;

        String address = "foo";
        Double latitude = 1.0;
        Double longitude = 1.0;

        UserDoesntExistException userDoesntExistException =
                assertThrows(UserDoesntExistException.class, ()-> userService.addLocationTo(id, address, latitude, longitude));
        assertNotNull(userDoesntExistException);
        assertEquals("User with id " + id + " does not exist", userDoesntExistException.getMessage());
    }

    @Test
    public void serviceThrowsUserDoesntExistExceptionWhenTryingToRetrieveAllLocationsWithAnInvalidUserId(){
        Long id = 2L;

        UserDoesntExistException userDoesntExistException =
                assertThrows(UserDoesntExistException.class, ()-> userService.getLocationsOf(id));
        assertNotNull(userDoesntExistException);
        assertEquals("User with id " + id + " does not exist", userDoesntExistException.getMessage());
    }

}
