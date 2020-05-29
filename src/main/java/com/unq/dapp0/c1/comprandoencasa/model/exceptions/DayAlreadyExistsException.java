package com.unq.dapp0.c1.comprandoencasa.model.exceptions;

import java.time.DayOfWeek;

public class DayAlreadyExistsException extends RuntimeException {
    public DayAlreadyExistsException(DayOfWeek day) {
        super("Day " + day.name() + " is already present");
    }
}
