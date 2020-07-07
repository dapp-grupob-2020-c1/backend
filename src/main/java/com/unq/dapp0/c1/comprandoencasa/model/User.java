package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.LocationAlreadyPresentException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    protected String name;
    @Column
    protected String password;
    @Column
    protected String email;
    @OneToMany
    private List<Location> locations;
    @Column
    private BigDecimal totalThreshold;
    @ElementCollection
    private Map<ProductType, BigDecimal> typesThreshold;
    @OneToOne
    private ShoppingList activeShoppingList;
    @OneToMany
    private List<ShoppingList> historicShoppingLists;

    public User() {}

    public User(String name, String password, String email) {
        this.checkNoEmpty(name, email, password);
        this.checkEmailFormat(email);

        this.name = name;
        this.password = password;
        this.email = email;
        this.locations = new ArrayList<>();
        this.historicShoppingLists = new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private void checkNoEmpty(String name, String email, String password) {
        if (name.isEmpty()){
            throw new EmptyFieldException("name");
        } else if (email.isEmpty()){
            throw new EmptyFieldException("email");
        } else if (password.isEmpty()){
            throw new EmptyFieldException("password");
        }
    }

    private void checkEmailFormat(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (!email.matches(regex)) {
            throw new InvalidEmailFormatException();
        }
    }

    public void validate(String password, String email) throws Exception {
        if (!this.email.equals(email) || !this.password.equals(password)) {
            throw new InvalidUserException();
        }
    }

    public void validate(User user) throws Exception {
        user.validate(this.password, this.email);
    }

    public String getName(){
        return this.name;
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
        this.findLocation(location).ifPresent(this.locations::remove);
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
