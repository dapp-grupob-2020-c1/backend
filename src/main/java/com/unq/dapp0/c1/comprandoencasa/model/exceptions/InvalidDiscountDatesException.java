package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

import java.time.LocalDate;

public class InvalidDiscountDatesException extends RuntimeException {
    public InvalidDiscountDatesException(LocalDate startingDate, LocalDate endingDate) {
        super("The dates given for the discount are invalid. Starting date is " + startingDate.toString() + " and ending date is " + endingDate.toString());
    }
}
