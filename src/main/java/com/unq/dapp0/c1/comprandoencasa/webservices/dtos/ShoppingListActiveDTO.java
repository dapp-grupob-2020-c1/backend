package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingList;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingListEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListActiveDTO {
    public Long id;
    public Location location;
    public List<ShoppingListEntryDTO> entries;
    public BigDecimal total;
    public BigDecimal totalThreshold;
    public List<ThresholdDTO> thresholdsByType;

    public ShoppingListActiveDTO(){}

    public ShoppingListActiveDTO(ShoppingList shoppingList, BigDecimal totalThreshold, List<ThresholdDTO> thresholdDTOList) {
        this.id = shoppingList.getId();
        this.location = shoppingList.getDeliveryLocation();
        this.entries = parseEntries(shoppingList.getEntriesList());
        this.total = shoppingList.totalValue();
        this.totalThreshold = totalThreshold;
        this.thresholdsByType = thresholdDTOList;
    }

    private List<ShoppingListEntryDTO> parseEntries(List<ShoppingListEntry> entriesList) {
        List<ShoppingListEntryDTO> returnList = new ArrayList<>();
        for (ShoppingListEntry entry : entriesList){
            returnList.add(new ShoppingListEntryDTO(entry));
        }
        return returnList;
    }
}
