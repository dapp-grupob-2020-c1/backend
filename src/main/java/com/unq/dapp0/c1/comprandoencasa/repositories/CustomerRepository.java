package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Configuration
@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findById(Long id);

    @Query(value = "select c from Customer c " +
            "where c.name = :name " +
            "or c.email = :email ")
    List<Customer> findByNameOrEmail(@Param(value = "name") String name,
                                     @Param(value = "email") String email);
}
