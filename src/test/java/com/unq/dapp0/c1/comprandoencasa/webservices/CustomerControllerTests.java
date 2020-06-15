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
        assertEquals(201, status);

        String content = mvcResult.getResponse().getContentAsString();
        CustomerOkDTO result = super.mapFromJson(content, CustomerOkDTO.class);
        assertEquals(name, result.name);
        assertEquals(customer.getId(), result.id);
    }

    @Test
    public void endpointPOSTCustomerReturnsBadRequestOnMissingOrEmptyParametersOrBadEmailFormat() throws Exception {
        String generic = "foo";

        errorTestWith(
                this.mockMvc.perform(
                post("/api/customer")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'name' is not present"
                );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer")
                                .param("name", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                        "Required String parameter 'email' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer")
                                .param("name", generic)
                                .param("email", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'password' is not present"
        );

        when(service.createCustomer(generic, generic, generic)).thenThrow(new InvalidEmailFormatException());

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer")
                                .param("name", generic)
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The email needs to follow the format: user@provider.com"
        );

        when(service.createCustomer("", generic, generic)).thenThrow(new EmptyFieldException("name"));
        when(service.createCustomer(generic, "", generic)).thenThrow(new EmptyFieldException("email"));
        when(service.createCustomer(generic, generic, "")).thenThrow(new EmptyFieldException("password"));

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer")
                                .param("name", "")
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field name is empty"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer")
                                .param("name", generic)
                                .param("email", "")
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field email is empty"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer")
                                .param("name", generic)
                                .param("email", generic)
                                .param("password", "")
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field password is empty"
        );
    }

    @Test
    public void endpointPOSTCustomerReturnsForbiddenIfEmailOrNameAlreadyExist() throws Exception {
        String generic = "foo";

        when(service.createCustomer(generic, generic, generic))
                .thenThrow(new FieldAlreadyExistsException("name"))
                .thenThrow(new FieldAlreadyExistsException("email"));

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer")
                                .param("name", generic)
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                403,
                "The field name already exists"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer")
                                .param("name", generic)
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                403,
                "The field email already exists"
        );
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

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer")
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'email' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer")
                                .param("email", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'password' is not present"
        );

        when(service.validateCustomer(generic, generic)).thenThrow(new InvalidEmailFormatException());

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer")
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The email needs to follow the format: user@provider.com"
        );

        when(service.validateCustomer("", generic)).thenThrow(new EmptyFieldException("email"));
        when(service.validateCustomer(generic, "")).thenThrow(new EmptyFieldException("password"));

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer")
                                .param("email", "")
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field email is empty"
        );

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer")
                                .param("email", generic)
                                .param("password", "")
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field password is empty"
        );
    }

    @Test
    public void endpointGETCustomerReturnsForbiddenIfEmailOrNameAlreadyExist() throws Exception {
        String generic = "foo";

        when(service.validateCustomer(generic, generic)).thenThrow(new InvalidUserException());

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer")
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                403,
                "Incorrect email or password"
        );
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
        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer/locations")
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'customerId' is not present"
        );

        String emptyId = "";

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer/locations")
                                .param("customerId", emptyId)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field customerId is empty"
        );
    }

    @Test
    public void endpointGETCustomerLocationsReturnsNotFoundIfNoCustomerIsFound() throws Exception {
        Long id = 1L;

        when(service.getLocationsOf(id)).thenThrow(new CustomerDoesntExistException(id));

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer/locations")
                                .param("customerId", String.valueOf(id))
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                404,
                "Customer with id " + id + " does not exist"
        );
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

        errorTestWith(
                this.mockMvc.perform(
                post("/api/customer/location")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'customerId' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer/location")
                                .param("customerId", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'address' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer/location")
                                .param("customerId", generic)
                                .param("address", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'latitude' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer/location")
                                .param("customerId", generic)
                                .param("address", generic)
                                .param("latitude", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'longitude' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer/location")
                                .param("customerId", "")
                                .param("address", generic)
                                .param("latitude", generic)
                                .param("longitude", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field customerId is empty"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer/location")
                                .param("customerId", generic)
                                .param("address", "")
                                .param("latitude", generic)
                                .param("longitude", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field address is empty"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer/location")
                                .param("customerId", generic)
                                .param("address", generic)
                                .param("latitude", "")
                                .param("longitude", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field latitude is empty"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer/location")
                                .param("customerId", generic)
                                .param("address", generic)
                                .param("latitude", generic)
                                .param("longitude", "")
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field longitude is empty"
        );
    }

    @Test
    public void endpointPOSTCustomerLocationReturnsNotFoundIfCustomerIdDoesntExist() throws Exception {
        String generic = "2";
        Long id = 1L;

        when(service.addLocationTo(id, generic, Double.valueOf(generic), Double.valueOf(generic)))
                .thenThrow(new CustomerDoesntExistException(id));

        errorTestWith(
                this.mockMvc.perform(
                        post("/api/customer/location")
                                .param("customerId", String.valueOf(id))
                                .param("address", generic)
                                .param("latitude", generic)
                                .param("longitude", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                404,
                "Customer with id " + id + " does not exist"
        );
    }
}
