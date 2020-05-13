package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

import com.unq.dapp0.c1.comprandoencasa.model.PaymentMethod;

public class PaymentMethodAlreadyExistsException extends RuntimeException{
    public PaymentMethodAlreadyExistsException(PaymentMethod paymentMethod){
        super("Payment method "+ paymentMethod.name()+" already exists");
    }
}
