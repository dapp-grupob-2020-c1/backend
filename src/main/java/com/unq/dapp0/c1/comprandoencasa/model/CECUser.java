package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
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
        this.checkNoEmpty(name, email, password);
        this.checkEmailFormat(email);

        this.name = name;
        this.password = password;
        this.email = email;
    }

    private void checkNoEmpty(String name, String email, String password) {
        if (name.isEmpty()){
            throw new EmptyFieldException("name");
        } else if (email.isEmpty()){
            throw new EmptyFieldException("email");
        } else if (password.isEmpty()){
            throw new EmptyFieldException("password");
        }
    }

    private void checkEmailFormat(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            throw new InvalidEmailFormatException();
        }
    }

    protected void validate(String password, String email, Exception exception) throws Exception {
        if (!this.email.equals(email) || !this.password.equals(password)) {
            throw exception;
        }
    }

    public String getName(){
        return this.name;
    }
}
