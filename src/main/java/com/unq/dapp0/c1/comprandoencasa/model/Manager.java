package com.unq.dapp0.c1.comprandoencasa.model;

/**
 * Representation of a Shop Manager. Holds validation methods for login and transactions.
 */
public class Manager extends CECUser {
    private final Shop shop;

    public Manager(String name, String password, String email, Shop shop) {
        super(name, password, email);
        this.shop = shop;
    }

    /**
     * Validates that the Manager is the same.
     * @throws InvalidManagerException when fails the validation
     * @param manager to validate.
     */
    public void validate(Manager manager) throws Exception {
        manager.validate(this.name, this.password, this.email);
    }

    /**
     * Validates access data for the manager.
     * @throws InvalidManagerException when fails the validation
     * @param name to validate.
     * @param password to validate.
     * @param email
     */
    public void validate(String name, String password, String email) throws Exception {
        this.validate(name, password, email, new InvalidManagerException());
    }

    public Shop getShop() {
        return this.shop;
    }
}
