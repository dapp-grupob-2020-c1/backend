package com.unq.dapp0.c1.comprandoencasa.webservices;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.services.CustomerService;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @CrossOrigin
    @PostMapping("/api/customer")
    public ResponseEntity<CustomerOkDTO> getShop(@RequestParam(value = "name") String name,
                                                 @RequestParam(value = "email") String email,
                                                 @RequestParam(value = "password") String password) {
        try{
            Customer customer = this.customerService.createCustomer(name, email, password);
            return new ResponseEntity<>(new CustomerOkDTO(customer), HttpStatus.CREATED);
        } catch (RuntimeException exception){
            if (exception instanceof InvalidEmailFormatException){
                throw new EmailFormatBadRequestException();
            } else if (exception instanceof EmptyFieldException){
                throw new EmptyFieldsBadRequestException(exception.getMessage());
            } else if (exception instanceof FieldAlreadyExistsException) {
                throw new FieldAlreadyExistsForbiddenException(exception.getMessage());
            } else {
                throw exception;
            }
        }
    }
}
