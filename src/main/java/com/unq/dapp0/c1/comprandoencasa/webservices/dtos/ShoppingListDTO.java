package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingListEntry;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListDTO {
    public Location location;
    public List<ShoppingListEntryDTO> entries;

    public ShoppingListDTO(){}

    public ShoppingListDTO(ShoppingList shoppingList) {
        this.location = shoppingList.getDeliveryLocation();
        this.entries = parseEntries(shoppingList.getEntriesList());
    }

    private List<ShoppingListEntryDTO> parseEntries(List<ShoppingListEntry> entriesList) {
        List<ShoppingListEntryDTO> returnList = new ArrayList<>();
        for (ShoppingListEntry entry : entriesList){
            returnList.add(new ShoppingListEntryDTO(entry));
        }
        return returnList;
    }
}
