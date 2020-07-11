package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.LocationBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.User;
import com.unq.dapp0.c1.comprandoencasa.model.UserBuilder;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.UserService;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.UserDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.UserOkDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.LocationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.payload.ApiResponse;
import com.unq.dapp0.c1.comprandoencasa.webservices.payload.SignUpRequest;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.AuthController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        UserController.class,
        AuthController.class,
        TestSecurityConfiguration.class
})
public class UserControllerTests extends AbstractRestTest{

    @Autowired
    private UserController userController;

    @Autowired
    private AuthController authController;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(userController).isNotNull();
        assertThat(authController).isNotNull();
    }

    @Test
    public void endpointGETUserMeReturnsDataOfTheCurrentUser() throws Exception {
        User mockUser = UserBuilder.anyUser().build();
        mockUser.setId(1L);

        when(service.findUserById(any())).thenReturn(mockUser);
        when(service.findUserByEmail(any())).thenReturn(java.util.Optional.of(mockUser));

        String generic = "test";

        UserPrincipal userDetails = new UserPrincipal(1L, generic, generic, new ArrayList<>());

        MvcResult mvcResult = this.mockMvc.perform(
                get("/user/me")
                        .with(user(userDetails)))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String content = mvcResult.getResponse().getContentAsString();
        UserDTO result = super.mapFromJson(content, UserDTO.class);

        assertEquals(mockUser.getName(), result.name);
        assertEquals(mockUser.getEmail(), result.email);
    }

    @Test
    public void endpointPOSTAuthSignupReturnsCreatedIfDataMatchesForNewUserCreation() throws Exception {
        String generic = "test";
        String genericEmail = generic+"@"+generic+"."+generic;

        User customer = mock(User.class);
        when(customer.getId()).thenReturn(1L);
        when(customer.getName()).thenReturn(generic);

        when(service.registerUser(genericEmail, generic, generic)).thenReturn(customer);

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail(genericEmail);
        signUpRequest.setName(generic);
        signUpRequest.setPassword(generic);

        String json = mapToJson(signUpRequest);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/auth/signup")
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        ApiResponse response = mapFromJson(content, ApiResponse.class);

        assertEquals(201, status);
        assertTrue(response.isSuccess());
        assertEquals("User registered successfully@", response.getMessage());
    }

    /* TODO: Terminar de adaptar estos tests
    @Test
    public void endpointPOSTAuthSignupReturnsBadRequestOnMissingOrEmptyParametersOrBadEmailFormat() throws Exception {
        String generic = "foo";

        errorTestWith(
                this.mockMvc.perform(
                post("/auth/signup")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'name' is not present"
                );

        errorTestWith(
                this.mockMvc.perform(
                        post("/auth/signup")
                                .param("name", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                        "Required String parameter 'email' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/auth/signup")
                                .param("name", generic)
                                .param("email", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'password' is not present"
        );

        when(service.createUser(generic, generic, generic)).thenThrow(new InvalidEmailFormatException());

        errorTestWith(
                this.mockMvc.perform(
                        post("/auth/signup")
                                .param("name", generic)
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The email needs to follow the format: user@provider.com"
        );

        when(service.createUser("", generic, generic)).thenThrow(new EmptyFieldException("name"));
        when(service.createUser(generic, "", generic)).thenThrow(new EmptyFieldException("email"));
        when(service.createUser(generic, generic, "")).thenThrow(new EmptyFieldException("password"));

        errorTestWith(
                this.mockMvc.perform(
                        post("/auth/signup")
                                .param("name", "")
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field name is empty"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/auth/signup")
                                .param("name", generic)
                                .param("email", "")
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field email is empty"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/auth/signup")
                                .param("name", generic)
                                .param("email", generic)
                                .param("password", "")
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field password is empty"
        );
    }

    @Test
    public void endpointPOSTUserReturnsForbiddenIfEmailOrNameAlreadyExist() throws Exception {
        String generic = "foo";

        when(service.createUser(generic, generic, generic))
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
    public void endpointGETUserReturnsOkIfValuesGivenValidateAnExistingUser() throws Exception {
        String generic = "foo";

        User customer = mock(User.class);
        when(customer.getId()).thenReturn(1L);
        when(customer.getName()).thenReturn(generic);

        when(service.validateUser(generic, generic)).thenReturn(customer);

        MvcResult mvcResult = this.mockMvc.perform(
                get("/api/customer")
                        .param("email", generic)
                        .param("password", generic)
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);

        UserOkDTO result = super.mapFromJson(content, UserOkDTO.class);
        assertEquals(generic, result.name);
        assertEquals(customer.getId(), result.id);
    }

    @Test
    public void endpointGETUserReturnsBadRequestOnMissingOrEmptyParametersOrBadEmailFormat() throws Exception {
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

        when(service.validateUser(generic, generic)).thenThrow(new InvalidEmailFormatException());

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer")
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The email needs to follow the format: user@provider.com"
        );

        when(service.validateUser("", generic)).thenThrow(new EmptyFieldException("email"));
        when(service.validateUser(generic, "")).thenThrow(new EmptyFieldException("password"));

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
    public void endpointGETUserReturnsForbiddenIfEmailOrNameAlreadyExist() throws Exception {
        String generic = "foo";

        when(service.validateUser(generic, generic)).thenThrow(new InvalidUserException());

        errorTestWith(
                this.mockMvc.perform(
                        get("/api/customer")
                                .param("email", generic)
                                .param("password", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                403,
                "Incorrect email or password"
        );
    }*/

    @WithMockUser("spring")
    @Test
    public void endpointGETUserLocationsReturnsOkWithAListOfLocations() throws Exception {
        Location location1 = LocationBuilder.anyLocation().build();
        location1.setId(2L);
        Location location2 = LocationBuilder.anyLocation().build();
        location2.setId(3L);

        List<Location> locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);

        when(service.getLocationsOf(1L)).thenReturn(locations);

        MvcResult mvcResult = this.mockMvc.perform(
                get("/user/locations")
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

    @WithMockUser("spring")
    @Test
    public void endpointGETUserLocationsReturnsBadRequestIfParameterUserIdIsEmptyOrMissing() throws Exception {
        errorTestWith(
                this.mockMvc.perform(
                        get("/user/locations")
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'customerId' is not present"
        );

        String emptyId = "";

        errorTestWith(
                this.mockMvc.perform(
                        get("/user/locations")
                                .param("customerId", emptyId)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field customerId is empty"
        );
    }

    @WithMockUser("spring")
    @Test
    public void endpointGETUserLocationsReturnsNotFoundIfNoUserIsFound() throws Exception {
        Long id = 1L;

        when(service.getLocationsOf(id)).thenThrow(new UserDoesntExistException(id));

        errorTestWith(
                this.mockMvc.perform(
                        get("/user/locations")
                                .param("customerId", String.valueOf(id))
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                404,
                "User with id " + id + " does not exist"
        );
    }

    @WithMockUser("spring")
    @Test
    public void endpointPOSTUserLocationReturnsCreated() throws Exception {
        Location location = LocationBuilder.anyLocation().build();
        location.setId(2L);

        String address = location.getAddress();
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        String customerId = "1";

        when(service.addLocationTo(Long.valueOf(customerId), address, latitude, longitude)).thenReturn(location);

        MvcResult mvcResult = this.mockMvc.perform(
                post("/user/location")
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

    @WithMockUser(username = "spring")
    @Test
    public void endpointPOSTUserLocationReturnsBadRequestIfThereIsAnyEmptyOrMissingParameter() throws Exception {
        String generic = "foo";

        errorTestWith(
                this.mockMvc.perform(
                post("/user/location")
                        .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'customerId' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/user/location")
                                .param("customerId", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'address' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/user/location")
                                .param("customerId", generic)
                                .param("address", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'latitude' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/user/location")
                                .param("customerId", generic)
                                .param("address", generic)
                                .param("latitude", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "Required String parameter 'longitude' is not present"
        );

        errorTestWith(
                this.mockMvc.perform(
                        post("/user/location")
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
                        post("/user/location")
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
                        post("/user/location")
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
                        post("/user/location")
                                .param("customerId", generic)
                                .param("address", generic)
                                .param("latitude", generic)
                                .param("longitude", "")
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                400,
                "The field longitude is empty"
        );
    }

    @WithMockUser("spring")
    @Test
    public void endpointPOSTUserLocationReturnsNotFoundIfUserIdDoesntExist() throws Exception {
        String generic = "2";
        Long id = 1L;

        when(service.addLocationTo(id, generic, Double.valueOf(generic), Double.valueOf(generic)))
                .thenThrow(new UserDoesntExistException(id));

        errorTestWith(
                this.mockMvc.perform(
                        post("/user/location")
                                .param("customerId", String.valueOf(id))
                                .param("address", generic)
                                .param("latitude", generic)
                                .param("longitude", generic)
                                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn(),
                404,
                "User with id " + id + " does not exist"
        );
    }
}
