package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

public class CartPurchaseDTO {
    @NotEmpty
    public Collection<ShopDeliveryCreationDTO> deliveries;
}
