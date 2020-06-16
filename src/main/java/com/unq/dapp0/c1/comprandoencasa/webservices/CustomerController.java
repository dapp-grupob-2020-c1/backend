package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.CustomerDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.CustomerService;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.CustomerOkDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.LocationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.EmailFormatBadRequestException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.EmptyFieldsBadRequestException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.FieldAlreadyExistsForbiddenException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.ValidationFailureForbiddenException;
import com.unq.dapp0.c1.comprandoencasa.webservices.exceptions.CustomerNotFoundException;
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
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @CrossOrigin
    @PostMapping("/api/customer")
    public ResponseEntity<CustomerOkDTO> createCustomer(@RequestParam(value = "name") String name,
                                                        @RequestParam(value = "email") String email,
                                                        @RequestParam(value = "password") String password) {
        try{
            Customer customer = this.customerService.createCustomer(name, email, password);
            return new ResponseEntity<>(new CustomerOkDTO(customer), HttpStatus.CREATED);
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
    public CustomerOkDTO validateCustomer(@RequestParam(value = "email") String email,
                                          @RequestParam(value = "password") String password) throws Exception {
        try{
            Customer customer = this.customerService.validateCustomer(email, password);
            return new CustomerOkDTO(customer);
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
            List<Location> locationList = this.customerService.getLocationsOf(Long.valueOf(customerId));
            return new LocationDTO(Long.valueOf(customerId), locationList);
        } catch (EmptyFieldException exception){
            throw new EmptyFieldsBadRequestException(exception.getMessage());
        } catch (CustomerDoesntExistException exception){
            throw new CustomerNotFoundException(exception.getMessage());
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
            Location location = this.customerService.addLocationTo(Long.valueOf(customerId),
                    address,
                    Double.valueOf(latitude),
                    Double.valueOf(longitude));
            return new ResponseEntity<Location>(location, HttpStatus.CREATED);
        } catch (EmptyFieldException exception){
            throw new EmptyFieldsBadRequestException(exception.getMessage());
        } catch (CustomerDoesntExistException exception){
            throw new CustomerNotFoundException(exception.getMessage());
        }
    }

    private void checkField(String field, String fieldName) {
        if (field.isEmpty()){
            throw new EmptyFieldException(fieldName);
        }
    }
}
