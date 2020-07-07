package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.User;

public class UserOkDTO {
    public Long id;
    public String name;

    public UserOkDTO(){}

    public UserOkDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
