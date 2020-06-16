package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidManagerException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Representation of a Shop Manager. Holds validation methods for login and transactions.
 */
@Entity
@Table
public class Manager extends CECUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Manager() {
        super();
    }

    public Manager(String name, String password, String email) {
        super(name, password, email);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Validates that the Manager is the same.
     *
     * @param manager to validate.
     * @throws InvalidManagerException when fails the validation
     */
    public void validate(Manager manager) throws Exception {
        manager.validate(this.name, this.password, this.email);
    }

    /**
     * Validates access data for the manager.
     *
     * @param name     to validate.
     * @param password to validate.
     * @param email
     * @throws InvalidManagerException when fails the validation
     */
    public void validate(String name, String password, String email) throws Exception {
        this.validate(name, password, email, new InvalidManagerException());
    }
}
