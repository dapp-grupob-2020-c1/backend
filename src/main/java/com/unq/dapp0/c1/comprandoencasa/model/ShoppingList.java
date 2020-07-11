package com.unq.dapp0.c1.comprandoencasa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Entity
@Table
public class ShoppingList {

    @OneToOne
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<ShoppingListEntry> entries;

    @OneToOne
    private Location deliveryLocation;

    public ShoppingList(){}

    public ShoppingList(Location deliveryLocation, User user) {
        this.entries = new ArrayList<>();
        this.deliveryLocation = deliveryLocation;
        this.user = user;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addProduct(Product aProduct, int aQuantity) {
        Optional<ShoppingListEntry> entry = getEntryFor(aProduct);
        if (entry.isPresent()){
            entry.get().setQuantity(aQuantity);
        } else {
            ShoppingListEntry newEntry = new ShoppingListEntry(aProduct, aQuantity);
            entries.add(newEntry);
        }
    }

    private Optional<ShoppingListEntry> getEntryFor(Product product) {
        return this.entries.stream().filter(entry -> entry.getProduct().getId().equals(product.getId())).findFirst();
    }

    public int countProducts() {
        return this.entries.size();
    }

    public int countItems() {
        int sum = 0;
        for (ShoppingListEntry shoppingListEntry : entries) {
            sum += shoppingListEntry.getQuantity();
        }
        return sum;
    }

    public BigDecimal totalValue() {
        HashSet<Shop> shops = new HashSet<>();
        this.entries.forEach(shoppingListEntry -> {
            Shop productShop = shoppingListEntry.getProduct().getShop();
            shops.add(productShop);
        });

        ArrayList<Discount> discounts = new ArrayList<>();
        shops.forEach(shop -> discounts.addAll(shop.getActiveDiscounts()));

        discounts.sort(Discount::compare);

        BigDecimal total = new BigDecimal(0);
        List<ShoppingListEntry> products = new ArrayList<>(this.entries);

        for (Discount discount : discounts) {
            total = discount.calculateFor(products);
        }

        for (ShoppingListEntry shoppingListEntry : entries) {
            BigDecimal productCalculatedPrice = shoppingListEntry.getProduct().getPrice();
            BigDecimal entryQuantity = BigDecimal.valueOf(shoppingListEntry.getQuantity());
            total = total.add(productCalculatedPrice.multiply(entryQuantity));
        }

        return total;
    }

    public List<ShoppingListEntry> getEntriesList() {
        if (entries == null){
            return new ArrayList<>();
        }
        return this.entries;
    }

    public Location getDeliveryLocation() {
        if (deliveryLocation == null){
            return new Location();
        }
        return this.deliveryLocation;
    }

    public void setDeliveryLocation(Location aLocation) {
        this.deliveryLocation = aLocation;
    }

    public User getUser() {
        if (user == null){
            return new User();
        }
        return this.user;
    }

    public BigDecimal evaluateTotalFor(ProductType productType) {
        BigDecimal total = new BigDecimal(0);
        for (ShoppingListEntry entry : this.entries) {
            Product product = entry.getProduct();
            if (product.isType(productType)) {
                Integer quantity = entry.getQuantity();
                total = total.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
        }
        return total;
    }
}
