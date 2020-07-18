package com.unq.dapp0.c1.comprandoencasa.services.exceptions;

import java.util.List;

public class EmailServiceExceptions extends RuntimeException {
    public EmailServiceExceptions(List<String> exceptions) {
        super("There were errors during mail sending: ::: " + String.join(" ::: and ::: ", exceptions));
    }
}
