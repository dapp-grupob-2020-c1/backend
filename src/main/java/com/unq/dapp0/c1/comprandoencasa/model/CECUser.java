package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;

import javax.persistence.Column;

public abstract class CECUser {
    @Column
    protected String name;
    @Column
    protected String password;
    @Column
    protected String email;

    public CECUser() {
    }

    public CECUser(String name, String password, String email) {
        this.checkEmailFormat(email);

        this.name = name;
        this.password = password;
        this.email = email;
    }

    private void checkEmailFormat(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            throw new InvalidEmailFormatException();
        }
    }

    protected void validate(String name, String password, String email, Exception exception) throws Exception {
        if (!this.name.equals(name) && !this.email.equals(email) && !this.password.equals(password)) {
            throw exception;
        }
    }
}
