package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.LocationAlreadyPresentException;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity
@Table
public class Customer extends CECUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private final List<Location> locations;

    @Column
    private BigDecimal totalThreshold;

    @ElementCollection
    private Map<ProductType, BigDecimal> typesThreshold;

    @OneToOne
    private ShoppingList activeShoppingList;

    @OneToMany
    private final List<ShoppingList> historicShoppingLists;

    public Customer(String name, String password, String email) {
        super(name, password, email);
        this.locations = new ArrayList<>();
        this.historicShoppingLists = new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void validate(String name, String password, String email) throws Exception {
        this.validate(name, password, email, new InvalidUserException());
    }

    public List<Location> getLocations() {
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
        return this.locations.stream().filter(loc -> loc.getId().equals(location.getId())).findFirst();
    }

    public BigDecimal getTotalThreshold() {
        return this.totalThreshold;
    }

    public void setTotalThreshold(BigDecimal threshold) {
        this.totalThreshold = threshold;
    }

    public Map<ProductType, BigDecimal> getTypesThreshold() {
        return this.typesThreshold;
    }

    public void setTypesThreshold(Map<ProductType, BigDecimal> typeList) {
        this.typesThreshold = typeList;
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

    public List<ShoppingList> getHistoricShoppingLists() {
        return this.historicShoppingLists;
    }

    public Dictionary<ProductType, BigDecimal> evaluateHistoricTypeThresholds() {
        Dictionary<ProductType, BigDecimal> returnThresholds = new Hashtable<>();
        for (ProductType productType : ProductType.values()) {
            returnThresholds.put(productType, this.evaluateForShoppingList(productType));
        }
        return returnThresholds;
    }

    private BigDecimal evaluateForShoppingList(ProductType productType) {
        BigDecimal total = new BigDecimal(0);
        int numberOfLists = 0;
        for (ShoppingList shoppingList : this.historicShoppingLists) {
            BigDecimal value = shoppingList.evaluateTotalFor(productType);
            if (value.compareTo(BigDecimal.valueOf(0)) > 0) {
                numberOfLists += 1;
                total = total.add(value);
            }
        }
        if (numberOfLists > 0) {
            return total.divide(BigDecimal.valueOf(numberOfLists));
        } else {
            return BigDecimal.valueOf(0);
        }
    }
}
