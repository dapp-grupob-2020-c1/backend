package com.unq.dapp0.c1.comprandoencasa.repositories;

import com.unq.dapp0.c1.comprandoencasa.model.Manager;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Configuration
@Repository
public interface ManagerRepositroy extends CrudRepository<Manager, Long> {

    Optional<Manager> findById(Long id);

    List<Manager> findAll();

}
