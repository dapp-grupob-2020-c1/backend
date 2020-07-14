package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

public class DeliveryDoesntExistException extends RuntimeException {
    public DeliveryDoesntExistException(Long deliveryId) {
        super("Delivery with id " + deliveryId + " does not exist.");
    }
}
