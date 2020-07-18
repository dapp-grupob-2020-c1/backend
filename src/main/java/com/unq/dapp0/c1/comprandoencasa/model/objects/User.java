package com.unq.dapp0.c1.comprandoencasa.model.objects;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.EmptyFieldException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidEmailFormatException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidUserException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.LocationAlreadyPresentException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.ShopAlreadyExistsException;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity
@Table
public class User {

    public static ShoppingList emptyShoppingList = new ShoppingList();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    protected String name;

    protected String password;

    @Email
    @Column(nullable = false)
    protected String email;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @OneToMany
    private List<Location> locations;

    @Column
    private BigDecimal totalThreshold;

    @ElementCollection
    private Map<ProductType, BigDecimal> typeThresholds;

    @ElementCollection
    private Map<ProductType, BigDecimal> suggestedTypeThresholds;

    @OneToOne
    private ShoppingList activeShoppingList;

    @OneToMany
    private List<ShoppingList> historicShoppingLists;

    @OneToMany
    private List<ShopDelivery> activeDeliveries;

    @OneToMany
    private List<ShopDelivery> historicDeliveries;

    @OneToMany
    private List<Shop> shops = new ArrayList<>();

    public User() {}

    public User(String name, String password, String email) {
        this.checkNoEmpty(name, email, password);
        this.checkEmailFormat(email);

        this.name = name;
        this.password = password;
        this.email = email;
        this.locations = new ArrayList<>();
        this.historicShoppingLists = new ArrayList<>();
        this.activeDeliveries = new ArrayList<>();
        this.historicDeliveries = new ArrayList<>();
        this.provider = AuthProvider.local;
        this.totalThreshold = BigDecimal.valueOf(0);
        this.typeThresholds = fillEmptyThresholds();
        this.suggestedTypeThresholds = fillEmptyThresholds();
    }

    private Map<ProductType, BigDecimal> fillEmptyThresholds() {
        Map<ProductType, BigDecimal> returnMap = new HashMap<>();
        for (ProductType type : ProductType.values()){
            returnMap.put(type, BigDecimal.valueOf(0));
        }
        return returnMap;
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

    public Map<ProductType, BigDecimal> getTypeThresholds() {
        return this.typeThresholds;
    }

    public void setTypeThresholds(Map<ProductType, BigDecimal> typeList) {
        this.typeThresholds = typeList;
    }

    public void setActiveShoppingList(ShoppingList shoppingList) {
        this.activeShoppingList = shoppingList;
    }

    public ShoppingList getActiveShoppingList() {
        if (activeShoppingList == null){
            return emptyShoppingList;
        }
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

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthProvider getProvider() {
        return this.provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String id) {
        this.providerId = id;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public List<Shop> getShops() {
        return this.shops;
    }

    public void addShop(Shop shop){
        Optional<Shop> exists = findShop(shop.getId());
        if (exists.isPresent()){
            throw new ShopAlreadyExistsException(shop);
        } else {
            this.shops.add(shop);
        }
    }

    public void removeShop(Shop shop){
        Optional<Shop> exists = findShop(shop.getId());
        exists.ifPresent(value -> this.shops.remove(value));
    }

    private Optional<Shop> findShop(Long id) {
        return this.shops.stream().filter(shop -> shop.getId().equals(id)).findFirst();
    }

    public List<ShopDelivery> getActiveDeliveries() {
        return activeDeliveries;
    }

    public void setActiveDeliveries(List<ShopDelivery> activeDeliveries) {
        this.activeDeliveries = activeDeliveries;
    }

    public List<ShopDelivery> getHistoricDeliveries() {
        return historicDeliveries;
    }

    public void setHistoricDeliveries(List<ShopDelivery> historicDeliveries) {
        this.historicDeliveries = historicDeliveries;
    }

    public Map<ProductType, BigDecimal> getSuggestedTypeThresholds() {
        return suggestedTypeThresholds;
    }

    public void setSuggestedTypeThresholds(Map<ProductType, BigDecimal> suggestedTypeThresholds) {
        this.suggestedTypeThresholds = suggestedTypeThresholds;
    }

    public void addNewDelivery(ShopDelivery delivery) {
        this.activeDeliveries.add(delivery);
    }

    public void finishPurchase() {
        this.historicShoppingLists.add(this.activeShoppingList);
        this.activeShoppingList = null;
    }

    public void confirmDeliveryReception(ShopDelivery delivery) {
        this.getActiveDeliveries().remove(delivery);
        this.getHistoricDeliveries().add(delivery);
    }

    public void cancelDelivery(ShopDelivery delivery) {
        this.activeDeliveries.remove(delivery);
    }
}
