package com.unq.dapp0.c1.comprandoencasa.model;

public class PaymentMethodAlreadyExistsException extends RuntimeException{
    public PaymentMethodAlreadyExistsException(PaymentMethod paymentMethod){
        super("Payment method "+ paymentMethod.name()+" already exists");
    }
}
