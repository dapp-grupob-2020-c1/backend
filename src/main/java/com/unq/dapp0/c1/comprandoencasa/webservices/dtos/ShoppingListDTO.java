package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingListEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListDTO {
    public Long id;
    public Location location;
    public List<ShoppingListEntryDTO> entries;
    public BigDecimal total;

    public ShoppingListDTO(){}

    public ShoppingListDTO(ShoppingList shoppingList) {
        this.id = shoppingList.getId();
        this.location = shoppingList.getDeliveryLocation();
        this.entries = parseEntries(shoppingList.getEntriesList());
        this.total = shoppingList.totalValue();
    }

    public static List<ShoppingListDTO> parseList(List<ShoppingList> shoppingLists) {
        List<ShoppingListDTO> returnList = new ArrayList<>();
        for (ShoppingList list : shoppingLists){
            returnList.add(new ShoppingListDTO(list));
        }
        return returnList;
    }

    private List<ShoppingListEntryDTO> parseEntries(List<ShoppingListEntry> entriesList) {
        List<ShoppingListEntryDTO> returnList = new ArrayList<>();
        for (ShoppingListEntry entry : entriesList){
            returnList.add(new ShoppingListEntryDTO(entry));
        }
        return returnList;
    }
}
