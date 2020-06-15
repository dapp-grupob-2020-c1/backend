package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class CustomerServiceTests {

    @Autowired
    private CustomerService customerService;

    @Test
    public void serviceCanCreateANewCustomerSaveItAndRetrieveIt(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        Customer customer = customerService.createCustomer(name, email, password);

        Customer retrievedCustomer = customerService.findCustomerById(customer.getId());

        assertEquals(customer.getId(), retrievedCustomer.getId());
        assertEquals(customer.getName(), retrievedCustomer.getName());
    }

    @Test
    public void serviceThrowsCustomerDoesntExistWhenTryingToFindByInexistentId(){
        Exception exception = assertThrows(CustomerDoesntExistException.class, ()->customerService.findCustomerById(0L));
        assertNotNull(exception);
        assertEquals("Customer with id 0 does not exist", exception.getMessage());
    }

    @Test
    public void serviceThrowsFieldAlreadyExistsIfTheNameOrEmailAreAlreadyInUse(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        customerService.createCustomer(name, email, password);

        Exception exceptionName = assertThrows(FieldAlreadyExistsException.class, ()->customerService.createCustomer(name, email, password));
        assertNotNull(exceptionName);
        assertEquals("The field name already exists", exceptionName.getMessage());

        Exception exceptionEmail = assertThrows(FieldAlreadyExistsException.class, ()->customerService.createCustomer("other", email, password));
        assertNotNull(exceptionEmail);
        assertEquals("The field email already exists", exceptionEmail.getMessage());
    }

    @Test
    public void serviceThrowsInvalidEmailFormatIfTheEmailFormatIsWrongDuringCreation(){
        String name = "foo";
        String email = "foo";
        String password = "1234";

        Exception exception = assertThrows(InvalidEmailFormatException.class, ()->customerService.createCustomer(name, email, password));
        assertNotNull(exception);
        assertEquals("The email format is invalid", exception.getMessage());
    }

    @Test
    public void serviceThrowsEmptyFieldExceptionIfEitherNameEmailOrPasswordAreEmptyDuringCreation(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        Exception emptyNameException = assertThrows(EmptyFieldException.class, ()->customerService.createCustomer("", email, password));
        assertNotNull(emptyNameException);
        assertEquals("The field name is empty", emptyNameException.getMessage());

        Exception emptyEmailException = assertThrows(EmptyFieldException.class, ()->customerService.createCustomer(name, "", password));
        assertNotNull(emptyEmailException);
        assertEquals("The field email is empty", emptyEmailException.getMessage());

        Exception emptyPassException = assertThrows(EmptyFieldException.class, ()->customerService.createCustomer(name, email, ""));
        assertNotNull(emptyPassException);
        assertEquals("The field password is empty", emptyPassException.getMessage());
    }

    @Test
    public void serviceCanValidateACustomerWithAGivenEmailAndPassword() throws Exception {
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        Customer customer = customerService.createCustomer(name, email, password);

        Customer retrievedCustomer = customerService.validateCustomer(email, password);

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

        customerService.createCustomer(name, email1, password1);

        Exception noEmailException = assertThrows(InvalidUserException.class, ()->customerService.validateCustomer(email2, password1));
        assertNotNull(noEmailException);
        assertEquals("The user, email or password are incorrect", noEmailException.getMessage());

        Exception badPasswordException = assertThrows(InvalidUserException.class, ()->customerService.validateCustomer(email1, password2));
        assertNotNull(badPasswordException);
        assertEquals("The user, email or password are incorrect", badPasswordException.getMessage());
    }

    @Test
    public void serviceThrowsInvalidEmailFormatIfTheEmailFormatIsWrongDuringValidation(){
        String email = "foo";
        String password = "1234";

        Exception exception = assertThrows(InvalidEmailFormatException.class, ()->customerService.validateCustomer(email, password));
        assertNotNull(exception);
        assertEquals("The email format is invalid", exception.getMessage());
    }

    @Test
    public void serviceThrowsEmptyFieldExceptionIfEitherEmailOrPasswordAreEmptyDuringValidation(){
        String email = "foo@foo.com";
        String password = "1234";

        Exception emptyEmailException = assertThrows(EmptyFieldException.class, ()->customerService.validateCustomer("", password));
        assertNotNull(emptyEmailException);
        assertEquals("The field email is empty", emptyEmailException.getMessage());

        Exception emptyPassException = assertThrows(EmptyFieldException.class, ()->customerService.validateCustomer(email, ""));
        assertNotNull(emptyPassException);
        assertEquals("The field password is empty", emptyPassException.getMessage());
    }

}
