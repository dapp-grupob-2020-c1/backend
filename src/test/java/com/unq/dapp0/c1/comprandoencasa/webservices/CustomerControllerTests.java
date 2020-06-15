package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.LocationBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.CustomerDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.CustomerService;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.CustomerOkDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.LocationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    public void endpointPOSTCustomerReturnsCreatedIfDataMatchesForNewCustomerCreation() throws Exception {
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

    @Test
    public void endpointGETCustomerReturnsOkIfValuesGivenValidateAnExistingCustomer() throws Exception {
        String generic = "foo";

        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1L);
        when(customer.getName()).thenReturn(generic);

        when(service.validateCustomer(generic, generic)).thenReturn(customer);

        MvcResult mvcResult = this.mockMvc.perform(
                get("/api/customer")
                        .param("email", generic)
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);

        CustomerOkDTO result = super.mapFromJson(content, CustomerOkDTO.class);
        assertEquals(generic, result.name);
        assertEquals(customer.getId(), result.id);
    }

    @Test
    public void endpointGETCustomerReturnsBadRequestOnMissingOrEmptyParametersOrBadEmailFormat() throws Exception {
        String generic = "foo";

        MvcResult noEmailResult = this.mockMvc.perform(
                get("/api/customer")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noEmailStatus = noEmailResult.getResponse().getStatus();
        assertEquals(400, noEmailStatus);

        String noEmailError = noEmailResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'email' is not present", noEmailError);

        MvcResult noPasswordResult = this.mockMvc.perform(
                get("/api/customer")
                        .param("email", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noPasswordStatus = noPasswordResult.getResponse().getStatus();
        assertEquals(400, noPasswordStatus);

        String noPasswordError = noPasswordResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'password' is not present", noPasswordError);

        when(service.validateCustomer(generic, generic)).thenThrow(new InvalidEmailFormatException());

        MvcResult badEmailFormatResult = this.mockMvc.perform(
                get("/api/customer")
                        .param("email", generic)
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int badEmailFormatStatus = badEmailFormatResult.getResponse().getStatus();
        assertEquals(400, badEmailFormatStatus);

        String badEmailFormatError = badEmailFormatResult.getResponse().getErrorMessage();
        assertEquals("The email needs to follow the format: user@provider.com", badEmailFormatError);

        when(service.validateCustomer("", generic)).thenThrow(new EmptyFieldException("email"));
        when(service.validateCustomer(generic, "")).thenThrow(new EmptyFieldException("password"));

        MvcResult emptyEmailResult = this.mockMvc.perform(
                get("/api/customer")
                        .param("email", "")
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int emptyEmailStatus = emptyEmailResult.getResponse().getStatus();
        assertEquals(400, emptyEmailStatus);

        String emptyEmailError = emptyEmailResult.getResponse().getErrorMessage();
        assertEquals("The field email is empty", emptyEmailError);

        MvcResult emptyPasswordResult = this.mockMvc.perform(
                get("/api/customer")
                        .param("email", generic)
                        .param("password", "")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int emptyPasswordStatus = emptyPasswordResult.getResponse().getStatus();
        assertEquals(400, emptyPasswordStatus);

        String emptyPasswordError = emptyPasswordResult.getResponse().getErrorMessage();
        assertEquals("The field password is empty", emptyPasswordError);
    }

    @Test
    public void endpointGETCustomerReturnsForbiddenIfEmailOrNameAlreadyExist() throws Exception {
        String generic = "foo";

        when(service.validateCustomer(generic, generic)).thenThrow(new InvalidUserException());

        MvcResult result = this.mockMvc.perform(
                get("/api/customer")
                        .param("email", generic)
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(403, status);

        String errorMessage = result.getResponse().getErrorMessage();
        assertEquals("Incorrect email or password", errorMessage);
    }

    @Test
    public void endpointGETCustomerLocationsReturnsOkWithAListOfLocations() throws Exception {
        Location location1 = LocationBuilder.anyLocation().build();
        location1.setId(2L);
        Location location2 = LocationBuilder.anyLocation().build();
        location2.setId(3L);

        List<Location> locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);

        when(service.getLocationsOf(1L)).thenReturn(locations);

        MvcResult mvcResult = this.mockMvc.perform(
                get("/api/customer/locations")
                        .param("customerId", String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        LocationDTO result = super.mapFromJson(content, LocationDTO.class);
        assertEquals(1L, result.customerId);
        assertEquals(2, result.locations.size());
        assertTrue(result.locations.stream().anyMatch(location -> location.getId().equals(location1.getId())));
        assertTrue(result.locations.stream().anyMatch(location -> location.getId().equals(location2.getId())));
    }

    @Test
    public void endpointGETCustomerLocationsReturnsBadRequestIfParameterCustomerIdIsEmptyOrMissing() throws Exception {
        MvcResult noIdResult = this.mockMvc.perform(
                get("/api/customer/locations")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noIdStatus = noIdResult.getResponse().getStatus();
        assertEquals(400, noIdStatus);

        String noIdError = noIdResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'customerId' is not present", noIdError);

        String emptyId = "";

        MvcResult emptyIdResult = this.mockMvc.perform(
                get("/api/customer/locations")
                        .param("customerId", emptyId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int emptyIdStatus = emptyIdResult.getResponse().getStatus();
        assertEquals(400, emptyIdStatus);

        String emptyIdError = emptyIdResult.getResponse().getErrorMessage();
        assertEquals("The field customerId is empty", emptyIdError);
    }

    @Test
    public void endpointGETCustomerLocationsReturnsNotFoundIfNoCustomerIsFound() throws Exception {
        Long id = 1L;

        when(service.getLocationsOf(id)).thenThrow(new CustomerDoesntExistException(id));

        MvcResult result = this.mockMvc.perform(
                get("/api/customer/locations")
                        .param("customerId", String.valueOf(id))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(404, status);

        String errorMessage = result.getResponse().getErrorMessage();
        assertEquals("Customer with id " + id + " does not exist", errorMessage);
    }

    @Test
    public void endpointPOSTCustomerLocationReturnsCreated() throws Exception {
        Location location = LocationBuilder.anyLocation().build();
        location.setId(2L);

        String address = location.getAddress();
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        String customerId = "1";

        when(service.addLocationTo(Long.valueOf(customerId), address, latitude, longitude)).thenReturn(location);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/api/customer/location")
                        .param("customerId", customerId)
                        .param("address", address)
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(201, status);

        Location result = super.mapFromJson(content, Location.class);
        assertEquals(location.getId(), result.getId());
    }

    @Test
    public void endpointPOSTCustomerLocationReturnsBadRequestIfThereIsAnyEmptyOrMissingParameter() throws Exception {
        String generic = "foo";

        MvcResult noCustomerIdResult = this.mockMvc.perform(
                post("/api/customer/location")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noCustomerIdStatus = noCustomerIdResult.getResponse().getStatus();
        assertEquals(400, noCustomerIdStatus);

        String noCustomerIdError = noCustomerIdResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'customerId' is not present", noCustomerIdError);

        MvcResult noAddressResult = this.mockMvc.perform(
                post("/api/customer/location")
                        .param("customerId", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noAddressStatus = noAddressResult.getResponse().getStatus();
        assertEquals(400, noAddressStatus);

        String noAddressError = noAddressResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'address' is not present", noAddressError);

        MvcResult noLatitudeResult = this.mockMvc.perform(
                post("/api/customer/location")
                        .param("customerId", generic)
                        .param("address", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noLatitudeStatus = noLatitudeResult.getResponse().getStatus();
        assertEquals(400, noLatitudeStatus);

        String noLatitudeError = noLatitudeResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'latitude' is not present", noLatitudeError);

        MvcResult noLongitudeResult = this.mockMvc.perform(
                post("/api/customer/location")
                        .param("customerId", generic)
                        .param("address", generic)
                        .param("latitude", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int noLongitudeStatus = noLongitudeResult.getResponse().getStatus();
        assertEquals(400, noLongitudeStatus);

        String noLongitudeError = noLongitudeResult.getResponse().getErrorMessage();
        assertEquals("Required String parameter 'longitude' is not present", noLongitudeError);

        MvcResult customerIdEmptyResult = this.mockMvc.perform(
                post("/api/customer/location")
                        .param("customerId", "")
                        .param("address", generic)
                        .param("latitude", generic)
                        .param("longitude", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int customerIdEmptyStatus = customerIdEmptyResult.getResponse().getStatus();
        assertEquals(400, customerIdEmptyStatus);

        String customerIdEmptyError = customerIdEmptyResult.getResponse().getErrorMessage();
        assertEquals("The field customerId is empty", customerIdEmptyError);

        MvcResult addressEmptyResult = this.mockMvc.perform(
                post("/api/customer/location")
                        .param("customerId", generic)
                        .param("address", "")
                        .param("latitude", generic)
                        .param("longitude", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int addressEmptyStatus = addressEmptyResult.getResponse().getStatus();
        assertEquals(400, addressEmptyStatus);

        String addressEmptyError = addressEmptyResult.getResponse().getErrorMessage();
        assertEquals("The field address is empty", addressEmptyError);

        MvcResult latitudeEmptyResult = this.mockMvc.perform(
                post("/api/customer/location")
                        .param("customerId", generic)
                        .param("address", generic)
                        .param("latitude", "")
                        .param("longitude", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int latitudeEmptyStatus = latitudeEmptyResult.getResponse().getStatus();
        assertEquals(400, latitudeEmptyStatus);

        String latitudeEmptyError = latitudeEmptyResult.getResponse().getErrorMessage();
        assertEquals("The field latitude is empty", latitudeEmptyError);

        MvcResult longitudeEmptyResult = this.mockMvc.perform(
                post("/api/customer/location")
                        .param("customerId", generic)
                        .param("address", generic)
                        .param("latitude", generic)
                        .param("longitude", "")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int longitudeEmptyStatus = longitudeEmptyResult.getResponse().getStatus();
        assertEquals(400, longitudeEmptyStatus);

        String longitudeEmptyError = longitudeEmptyResult.getResponse().getErrorMessage();
        assertEquals("The field longitude is empty", longitudeEmptyError);
    }

    @Test
    public void endpointPOSTCustomerLocationReturnsNotFoundIfCustomerIdDoesntExist() throws Exception {
        String generic = "2";
        Long id = 1L;

        when(service.addLocationTo(id, generic, Double.valueOf(generic), Double.valueOf(generic)))
                .thenThrow(new CustomerDoesntExistException(id));

        MvcResult result = this.mockMvc.perform(
                post("/api/customer/location")
                        .param("customerId", String.valueOf(id))
                        .param("address", generic)
                        .param("latitude", generic)
                        .param("longitude", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(404, status);

        String errorMessage = result.getResponse().getErrorMessage();
        assertEquals("Customer with id " + id + " does not exist", errorMessage);
    }
}
