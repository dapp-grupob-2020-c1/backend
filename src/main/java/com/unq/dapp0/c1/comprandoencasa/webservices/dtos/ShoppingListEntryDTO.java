package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.ShoppingListEntry;

public class ShoppingListEntryDTO {
    public ProductSmallDTO product;
    public Integer quantity;

    public ShoppingListEntryDTO(){}

    public ShoppingListEntryDTO(ShoppingListEntry entry) {
        this.product = new ProductSmallDTO(entry.getProduct());
        this.quantity = entry.getQuantity();
    }
}
