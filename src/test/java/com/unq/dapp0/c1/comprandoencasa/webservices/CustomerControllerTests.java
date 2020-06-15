package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.services.CustomerService;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTests extends AbstractRestTest{

    @Autowired
    private CustomerController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void endpointPOSTCustomerReturnsOkIfDataMatchesForNewCustomerCreation() throws Exception {
        String name = "jo";
        String email = "hola@adios.com";
        String password = "1234";

        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1L);
        when(customer.getName()).thenReturn(name);

        when(service.createCustomer(name, email, password)).thenReturn(customer);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/api/customer")
                        .param("name", name)
                        .param("email", email)
                        .param("password", password)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(201, status);

        CustomerOkDTO result = super.mapFromJson(content, CustomerOkDTO.class);
        assertEquals(name, result.name);
        assertEquals(customer.getId(), result.id);
    }

    @Test
    public void endpointPOSTCustomerReturnsBadRequestOnMissingOrEmptyParametersOrBadEmailFormat() throws Exception {
        String generic = "foo";

        MvcResult noNameResult = this.mockMvc.perform(
                post("/api/customer")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noNameStatus = noNameResult.getResponse().getStatus();
        assertEquals(400, noNameStatus);

        String noNameError = noNameResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'name' is not present", noNameError);
        
        MvcResult noEmailResult = this.mockMvc.perform(
                post("/api/customer")
                        .param("name", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noEmailStatus = noEmailResult.getResponse().getStatus();
        assertEquals(400, noEmailStatus);

        String noEmailError = noEmailResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'email' is not present", noEmailError);

        MvcResult noPasswordResult = this.mockMvc.perform(
                post("/api/customer")
                        .param("name", generic)
                        .param("email", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noPasswordStatus = noPasswordResult.getResponse().getStatus();
        assertEquals(400, noPasswordStatus);

        String noPasswordError = noPasswordResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'password' is not present", noPasswordError);

        when(service.createCustomer(generic, generic, generic)).thenThrow(new InvalidEmailFormatException());

        MvcResult badEmailFormatResult = this.mockMvc.perform(
                post("/api/customer")
                        .param("name", generic)
                        .param("email", generic)
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int badEmailFormatStatus = badEmailFormatResult.getResponse().getStatus();
        assertEquals(400, badEmailFormatStatus);

        String badEmailFormatError = badEmailFormatResult.getResponse().getErrorMessage();
        assertEquals("The email needs to follow the format: user@provider.com", badEmailFormatError);

        when(service.createCustomer("", generic, generic)).thenThrow(new EmptyFieldException("name"));
        when(service.createCustomer(generic, "", generic)).thenThrow(new EmptyFieldException("email"));
        when(service.createCustomer(generic, generic, "")).thenThrow(new EmptyFieldException("password"));

        MvcResult emptyNameResult = this.mockMvc.perform(
                post("/api/customer")
                        .param("name", "")
                        .param("email", generic)
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int emptyNameStatus = emptyNameResult.getResponse().getStatus();
        assertEquals(400, emptyNameStatus);

        String emptyNameError = emptyNameResult.getResponse().getErrorMessage();
        assertEquals("The field name is empty", emptyNameError);

        MvcResult emptyEmailResult = this.mockMvc.perform(
                post("/api/customer")
                        .param("name", generic)
                        .param("email", "")
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int emptyEmailStatus = emptyEmailResult.getResponse().getStatus();
        assertEquals(400, emptyEmailStatus);

        String emptyEmailError = emptyEmailResult.getResponse().getErrorMessage();
        assertEquals("The field email is empty", emptyEmailError);

        MvcResult emptyPasswordResult = this.mockMvc.perform(
                post("/api/customer")
                        .param("name", generic)
                        .param("email", generic)
                        .param("password", "")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int emptyPasswordStatus = emptyPasswordResult.getResponse().getStatus();
        assertEquals(400, emptyPasswordStatus);

        String emptyPasswordError = emptyPasswordResult.getResponse().getErrorMessage();
        assertEquals("The field password is empty", emptyPasswordError);
    }

    @Test
    public void endpointPOSTCustomerReturnsForbiddenIfEmailOrNameAlreadyExist() throws Exception {
        String generic = "foo";

        when(service.createCustomer(generic, generic, generic))
                .thenThrow(new FieldAlreadyExistsException("name"))
                .thenThrow(new FieldAlreadyExistsException("email"));

        MvcResult nameExistsResult = this.mockMvc.perform(
                post("/api/customer")
                        .param("name", generic)
                        .param("email", generic)
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int nameExistsStatus = nameExistsResult.getResponse().getStatus();
        assertEquals(403, nameExistsStatus);

        String nameExistsError = nameExistsResult.getResponse().getErrorMessage();
        assertEquals("The field name already exists", nameExistsError);

        MvcResult emailExistsResult = this.mockMvc.perform(
                post("/api/customer")
                        .param("name", generic)
                        .param("email", generic)
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int emailExistsStatus = emailExistsResult.getResponse().getStatus();
        assertEquals(403, emailExistsStatus);

        String emailExistsError = emailExistsResult.getResponse().getErrorMessage();
        assertEquals("The field email already exists", emailExistsError);
    }

}
