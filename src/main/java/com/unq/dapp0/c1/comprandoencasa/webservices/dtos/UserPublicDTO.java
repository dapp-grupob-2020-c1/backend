package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.User;

public class UserPublicDTO {
    public String name;
    public String email;
    public String imgUrl;

    public UserPublicDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.imgUrl = user.getImageUrl();
    }
}
