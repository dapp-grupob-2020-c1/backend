package com.unq.dapp0.c1.comprandoencasa.services;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;
import com.unq.dapp0.c1.comprandoencasa.repositories.CustomerRepository;
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
}
