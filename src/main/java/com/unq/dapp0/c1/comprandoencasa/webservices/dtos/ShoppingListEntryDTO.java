package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingListEntry;

public class ShoppingListEntryDTO {
    public Long id;
    public ProductSmallDTO product;
    public Integer quantity;

    public ShoppingListEntryDTO(){}

    public ShoppingListEntryDTO(ShoppingListEntry entry) {
        this.id = entry.getId();
        this.product = new ProductSmallDTO(entry.getProduct());
        this.quantity = entry.getQuantity();
    }
}
