package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void givenACorrectSetupthenAnEntityManagerWillBeAvailable() {
        assertNotNull(entityManager);
    }

    @Test
    public void repositoryCanSaveAndReturnAGivenCustomer(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        Customer customer = new Customer(name, password, email);
        customerRepository.save(customer);
        Optional<Customer> result = customerRepository.findById(customer.getId());
        assertTrue(result.isPresent());
        assertEquals(customer.getId(), result.get().getId());
    }

    @Test
    public void repositoryCanReturnAListOfCustomersThatMatchEitherNameOrEmail(){
        String name1 = "foo";
        String name2 = "faa";
        String email1 = "foo@foo.com";
        String email2 = "faa@foo.com";
        String password = "1234";

        Customer customer1 = new Customer(name1, password, email1);
        Customer customer2 = new Customer(name1, password, email2);
        Customer customer3 = new Customer(name2, password, email1);
        Customer customer4 = new Customer(name2, password, email2);

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);

        List<Customer> resultList = customerRepository.findByNameOrEmail(name1, email1);

        assertEquals(3, resultList.size());
        assertTrue(resultList.stream().anyMatch(customer -> customer.getId().equals(customer1.getId())));
        assertTrue(resultList.stream().anyMatch(customer -> customer.getId().equals(customer2.getId())));
        assertTrue(resultList.stream().anyMatch(customer -> customer.getId().equals(customer3.getId())));
    }

    @Test
    public void repositoryCanFindACustomerByItsEmail(){
        String name = "foo";
        String email = "foo@foo.com";
        String password = "1234";

        Customer customer = new Customer(name, password, email);
        customerRepository.save(customer);
        Optional<Customer> result = customerRepository.findByEmail(email);
        assertTrue(result.isPresent());
        assertEquals(customer.getId(), result.get().getId());
    }
}
