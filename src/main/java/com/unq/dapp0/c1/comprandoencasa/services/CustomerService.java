package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.repositories.CustomerRepository;
import com.unq.dapp0.c1.comprandoencasa.repositories.LocationRepository;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.CustomerDoesntExistException;
import com.unq.dapp0.c1.comprandoencasa.services.exceptions.FieldAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Transactional
    public Customer createCustomer(String name, String email, String password) {
        checkIfExists(name, email);
        Customer customer = new Customer(name, password, email);
        customerRepository.save(customer);
        return customer;
    }

    private void checkIfExists(String name, String email) {
        List<Customer> customerList = customerRepository.findByNameOrEmail(name, email);
        if (!customerList.isEmpty()){
            Customer customer = customerList.get(0);
            if (customer.getName().equals(name)){
                throw new FieldAlreadyExistsException("name");
            } else {
                throw new FieldAlreadyExistsException("email");
            }
        }
    }

    @Transactional
    public Customer findCustomerById(Long id) {
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        } else {
            throw new CustomerDoesntExistException(id);
        }
    }

    @Transactional
    public Customer validateCustomer(String email, String password) throws Exception {
        checkParameter(email, "email");
        checkParameter(password, "password");
        checkEmailFormat(email);
        Optional<Customer> result = customerRepository.findByEmail(email);
        if (result.isPresent()){
            Customer customer = result.get();
            customer.validate(password, email);
            return customer;
        } else {
            throw new InvalidUserException();
        }
    }

    private void checkParameter(String field, String fieldName) {
        if (field.isEmpty()){
            throw new EmptyFieldException(fieldName);
        }
    }

    private void checkEmailFormat(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            throw new InvalidEmailFormatException();
        }
    }

    @Transactional
    public List<Location> getLocationsOf(Long customerId) {
        Customer customer = findCustomerById(customerId);
        return customer.getLocations();
    }

    @Transactional
    public Location addLocationTo(Long customerId, String address, Double latitude, Double longitude) {
        checkParameter(address, "address");
        Customer customer = findCustomerById(customerId);
        Location location = new Location(address, latitude, longitude);
        customer.addLocation(location);
        locationRepository.save(location);
        customerRepository.save(customer);
        return location;
    }
}
