package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.User;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.UserDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.UserService;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.services.security.UserPrincipal;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.UserOkDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.LocationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.EmailFormatBadRequestException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.EmptyFieldsBadRequestException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.FieldAlreadyExistsForbiddenException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ValidationFailureForbiddenException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.UserNotFoundException;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.CurrentUser;
import com.unq.dapp0.c1.comprandoencasa.webservices.security.user.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.findUserById(userPrincipal.getId());
    }

    @CrossOrigin
    @GetMapping("/locations")
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
    @PostMapping("/location")
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
