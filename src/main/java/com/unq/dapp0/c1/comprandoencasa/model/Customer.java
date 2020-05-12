package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;
import java.util.*;

public class Customer extends CECUser {
    private ArrayList<Location> locations;
    private BigDecimal totalThreshold;
    private Dictionary<ProductType, BigDecimal> typesThreshold;
    private ShoppingList activeShoppingList;
    private ArrayList<ShoppingList> historicShoppingLists;

    public Customer(String name, String password, String email) {
        super(name, password, email);
        this.locations = new ArrayList<>();
        this.historicShoppingLists = new ArrayList<>();
    }

    public void validate(String name, String password, String email) throws Exception {
        this.validate(name, password, email,new InvalidUserException() );
    }

    public ArrayList<Location> getLocations() {
        return this.locations;
    }

    public void addLocation(Location location) {
        if (!this.findLocation(location).isPresent()) {
            this.locations.add(location);
        } else {
            throw new LocationAlreadyPresentException(location);
        }
    }

    public void removeLocation(Location location) {
        this.findLocation(location).ifPresent(value -> this.locations.remove(value));
    }

    private Optional<Location> findLocation(Location location) {
        return this.locations.stream().filter(loc -> loc.getID().equals(location.getID())).findFirst();
    }

    public void setTotalThreshold(BigDecimal threshold) {
        this.totalThreshold = threshold;
    }

    public BigDecimal getTotalThreshold() {
        return this.totalThreshold;
    }

    public void setTypesThreshold(Dictionary<ProductType, BigDecimal> typeList) {
        this.typesThreshold = typeList;
    }

    public Dictionary<ProductType, BigDecimal> getTypesThreshold() {
        return this.typesThreshold;
    }

    public void addActiveShoppingList(ShoppingList shoppingList) {
        this.activeShoppingList = shoppingList;
    }

    public ShoppingList getActiveShoppingList() {
        return this.activeShoppingList;
    }

    public void addHistoricShoppingList(ShoppingList shoppingList) {
        this.historicShoppingLists.add(shoppingList);
    }

    public ArrayList<ShoppingList> getHistoricShoppingLists() {
        return this.historicShoppingLists;
    }

    public Dictionary<ProductType, BigDecimal> evaluateHistoricTypeThresholds() {
        Dictionary<ProductType, BigDecimal> returnThresholds = new Hashtable<>();
        for (ProductType productType : ProductType.values()){
            returnThresholds.put(productType, this.evaluateForShoppingList(productType));
        }
        return returnThresholds;
    }

    private BigDecimal evaluateForShoppingList(ProductType productType) {
        BigDecimal total = new BigDecimal(0);
        int numberOfLists = 0;
        for (ShoppingList shoppingList : this.historicShoppingLists){
            BigDecimal value = shoppingList.evaluateTotalFor(productType);
            if (value.compareTo(BigDecimal.valueOf(0)) > 0){
                numberOfLists += 1;
                total = total.add(value);
            }
        }
        if (numberOfLists > 0){
            return total.divide(BigDecimal.valueOf(numberOfLists));
        } else {
            return BigDecimal.valueOf(0);
        }
    }
}
