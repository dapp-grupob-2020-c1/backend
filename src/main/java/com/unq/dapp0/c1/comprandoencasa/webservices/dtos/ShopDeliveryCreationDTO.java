package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.Turn;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ShopDeliveryCreationDTO {
    @NotBlank
    public Long shopId;
    @NotBlank
    public List<Long> products;
    @NotBlank
    public UserPublicDTO user;
    @NotBlank
    public Turn turn;
    @NotBlank
    public Location location;
}
