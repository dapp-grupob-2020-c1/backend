package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.objects.User;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopDeliveryCreationDTO;
import com.unq.dapp0.c1.comprandoencasa.webservices.dtos.ShopTakeawayCreationDTO;

public class MissingFieldsForDeliveryCreationException extends RuntimeException {
    public MissingFieldsForDeliveryCreationException(User user, ShopDeliveryCreationDTO deliveryDTO) {
        super("There are missing fields for delivery creation for user " + user.getId() + " for the shop " + deliveryDTO.shopId);
    }

    public MissingFieldsForDeliveryCreationException(User user, ShopTakeawayCreationDTO takeawayDTO) {
        super("There are missing fields for delivery creation for user " + user.getId() + " for the shop " + takeawayDTO.shopId);
    }
}
