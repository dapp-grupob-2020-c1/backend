package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import javax.validation.constraints.NotBlank;

public class ShoppingListEntryAddDTO {
    @NotBlank
    public Long productId;
    @NotBlank
    public int amount;
}
