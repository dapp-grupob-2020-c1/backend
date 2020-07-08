package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Configuration
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(Long id);

    @Query(value = "select u from User u " +
            "where u.name = :name " +
            "or u.email = :email ")
    List<User> findByNameOrEmail(@Param(value = "name") String name,
                                     @Param(value = "email") String email);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
