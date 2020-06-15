package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.Customer;

public class CustomerOkDTO {
    public Long id;
    public String name;

    public CustomerOkDTO(){}

    public CustomerOkDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
    }
}
