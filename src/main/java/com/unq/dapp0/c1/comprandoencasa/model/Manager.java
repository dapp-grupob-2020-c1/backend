package com.unq.dapp0.c1.comprandoencasa.model;

/**
 * Representation of a Shop Manager. Holds validation methods for login and transactions.
 */
public class Manager {
    private final String name;
    private final String password;
    private final Shop shop;
    private final String email;

    public Manager(String name, String password, String email, Shop shop) {
        this.checkEmailFormat(email);

        this.name = name;
        this.password = password;
        this.email = email;
        this.shop = shop;
    }

    private void checkEmailFormat(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            throw new InvalidEmailFormatException();
        }
    }

    /**
     * Validates that the Manager is the same.
     * @throws InvalidManagerException when fails the validation
     * @param manager to validate.
     */
    public void validate(Manager manager) {
        manager.validate(this.name, this.password, this.email);
    }

    /**
     * Validates access data for the manager.
     * @throws InvalidManagerException when fails the validation
     * @param name to validate.
     * @param password to validate.
     * @param email
     */
    public void validate(String name, String password, String email) {
        if (!(this.name.equals(name) && this.email.equals(email) && this.password.equals(password))){
            throw new InvalidManagerException();
        }
    }

    public Shop getShop() {
        return this.shop;
    }
}
