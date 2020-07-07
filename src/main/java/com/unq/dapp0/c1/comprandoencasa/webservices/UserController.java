package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.User;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.UserService;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.UserOkDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.LocationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.EmailFormatBadRequestException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.EmptyFieldsBadRequestException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.FieldAlreadyExistsForbiddenException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ValidationFailureForbiddenException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RestController
@EnableAutoConfiguration
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping("/api/customer")
    public ResponseEntity<UserOkDTO> createUser(@RequestParam(value = "name") String name,
                                                    @RequestParam(value = "email") String email,
                                                    @RequestParam(value = "password") String password) {
        try{
            User user = this.userService.createUser(name, email, password);
            return new ResponseEntity<>(new UserOkDTO(user), HttpStatus.CREATED);
        } catch (InvalidEmailFormatException exception){
            throw new EmailFormatBadRequestException();
        } catch (EmptyFieldException exception){
            throw new EmptyFieldsBadRequestException(exception.getMessage());
        } catch (FieldAlreadyExistsException exception){
            throw new FieldAlreadyExistsForbiddenException(exception.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/api/customer")
    public UserOkDTO validateUser(@RequestParam(value = "email") String email,
                                      @RequestParam(value = "password") String password) throws Exception {
        try{
            User user = this.userService.validateUser(email, password);
            return new UserOkDTO(user);
        } catch (InvalidEmailFormatException exception){
            throw new EmailFormatBadRequestException();
        } catch (EmptyFieldException exception){
            throw new EmptyFieldsBadRequestException(exception.getMessage());
        } catch (InvalidUserException exception){
            throw new ValidationFailureForbiddenException();
        }
    }

    @CrossOrigin
    @GetMapping("/api/customer/locations")
    public LocationDTO getLocations(@RequestParam(value = "customerId") String customerId) {
        try{
            if (customerId.isEmpty()){
                throw new EmptyFieldException("customerId");
            }
            List<Location> locationList = this.userService.getLocationsOf(Long.valueOf(customerId));
            return new LocationDTO(Long.valueOf(customerId), locationList);
        } catch (EmptyFieldException exception){
            throw new EmptyFieldsBadRequestException(exception.getMessage());
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    @CrossOrigin
    @PostMapping("/api/customer/location")
    public ResponseEntity<Location> createLocation(@RequestParam(value = "customerId") String customerId,
                                                   @RequestParam(value = "address") String address,
                                                   @RequestParam(value = "latitude") String latitude,
                                                   @RequestParam(value = "longitude") String longitude) {
        try{
            checkField(customerId, "customerId");
            checkField(address, "address");
            checkField(latitude, "latitude");
            checkField(longitude, "longitude");
            Location location = this.userService.addLocationTo(Long.valueOf(customerId),
                    address,
                    Double.valueOf(latitude),
                    Double.valueOf(longitude));
            return new ResponseEntity<Location>(location, HttpStatus.CREATED);
        } catch (EmptyFieldException exception){
            throw new EmptyFieldsBadRequestException(exception.getMessage());
        } catch (UserDoesntExistException exception){
            throw new UserNotFoundException(exception.getMessage());
        }
    }

    private void checkField(String field, String fieldName) {
        if (field.isEmpty()){
            throw new EmptyFieldException(fieldName);
        }
    }
}
