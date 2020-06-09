package com.unq.dapp0.c1.comprandoencasa.model;

import static org.mockito.Mockito.mock;

public class ManagerBuilder{
    private String name;
    private String password;
    private Shop shop;
    private String email;

    public static ManagerBuilder anyManager(){
        return new ManagerBuilder();
    }

    private ManagerBuilder(){
        this.name = "Test";
        this.password = "1234";
        this.email = "example@example.com";
        this.shop = mock(Shop.class);
    }

    public ManagerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ManagerBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public ManagerBuilder withShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public ManagerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public Manager build() {
        return new Manager(name, password, email, shop);
    }
}

