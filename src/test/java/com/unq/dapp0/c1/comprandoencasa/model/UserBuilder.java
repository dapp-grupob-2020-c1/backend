package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.objects.User;

public class UserBuilder {
    private String name;
    private String password;
    private String email;

    public static UserBuilder anyUser(){
        return new UserBuilder();
    }

    private UserBuilder(){
        this.name = "Test";
        this.email = "example@example.com";
        this.password = "1111";
    }

    public User build() {
        return new User(name, password, email);
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
}
