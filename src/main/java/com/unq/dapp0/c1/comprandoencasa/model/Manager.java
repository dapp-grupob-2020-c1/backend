package com.unq.dapp0.c1.comprandoencasa.model;

/**
 * Representation of a Shop Manager. Holds validation methods for login and transactions.
 */
public class Manager {
    private final String name;
    private final String password;
    private final Shop shop;

    public Manager(String name, String password, Shop shop) {
        this.name = name;
        this.password = password;
        this.shop = shop;
    }

    /**
     * Validates that the Manager is the same.
     * @throws InvalidManagerException when fails the validation
     * @param manager to validate.
     */
    public void validate(Manager manager) {
        manager.validate(this.name, this.password);
    }

    /**
     * Validates access data for the manager.
     * @throws InvalidManagerException when fails the validation
     * @param name to validate.
     * @param password to validate.
     */
    public void validate(String name, String password) {
        if (!(this.name.equals(name) && this.password.equals(password))){
            throw new InvalidManagerException();
        }
    }

    public Shop getShop() {
        return this.shop;
    }
}
