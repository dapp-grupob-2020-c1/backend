package com.unq.dapp0.c1.comprandoencasa.model;

public class ManagerBuilder{
    private String name;
    private String password;
    private String email;

    public static ManagerBuilder anyManager(){
        return new ManagerBuilder();
    }

    private ManagerBuilder(){
        this.name = "Test";
        this.password = "1234";
        this.email = "example@example.com";
    }

    public ManagerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ManagerBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public ManagerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public Manager build() {
        return new Manager(name, password, email);
    }
}

