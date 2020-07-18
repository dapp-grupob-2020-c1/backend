package com.unq.dapp0.c1.comprandoencasa.model.objects;

import com.unq.dapp0.c1.comprandoencasa.services.exceptions.ProductDoesntExistException;

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

    public ShoppingListEntry addProduct(Product aProduct, int aQuantity) {
        Optional<ShoppingListEntry> entryOptional = getEntryFor(aProduct);
        if (entryOptional.isPresent()){
            ShoppingListEntry entry = entryOptional.get();
            entry.setQuantity(aQuantity);
            return entry;
        } else {
            ShoppingListEntry newEntry = new ShoppingListEntry(aProduct, aQuantity);
            entries.add(newEntry);
            return newEntry;
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

    public BigDecimal totalValue() {
        BigDecimal total = new BigDecimal(0);
        for (ProductType type : ProductType.values()){
            total = total.add(evaluateTotalFor(type));
        }
        return total;
    }

    public BigDecimal evaluateTotalFor(ProductType productType) {
        BigDecimal total = new BigDecimal(0);
        for (ShoppingListEntry entry : this.entries) {
            Product product = entry.getProduct();
            if (product.isType(productType)) {
                total = total.add(calculateFor(entry, entries));
            }
        }
        return total;
    }

    private BigDecimal calculateFor(ShoppingListEntry entry, List<ShoppingListEntry> entries) {
        BigDecimal total = new BigDecimal(0);
        Product product = entry.getProduct();
        List<Discount> discounts = product.getShop().getActiveDiscounts();
        discounts.sort(Discount::compare);
        for (int amountEvaluated=0; amountEvaluated < entry.getQuantity(); amountEvaluated++){
            Optional<Discount> currentDiscount = findDiscount(product, entries, amountEvaluated, discounts);
            if (currentDiscount.isPresent()){
                total = total.add(currentDiscount.get().calculateFor(entry, entries));
            } else {
                total = total.add(product.getPrice());
            }
        }
        return total;
    }

    private Optional<Discount> findDiscount(Product product, List<ShoppingListEntry> entries, int amountEvaluated, List<Discount> discounts) {
        for (Discount discount : discounts){
            if (discount.isValidFor(product, entries, amountEvaluated)){
                return Optional.of(discount);
            }
        }
        return Optional.empty();
    }

    public void removeEntry(ShoppingListEntry entry) {
        this.entries.remove(entry);
    }

    public ShoppingListEntry removeProduct(Product product) {
        Optional<ShoppingListEntry> entryOptional = this.entries.stream()
                .filter(entry -> entry.getProduct().getId().equals(product.getId())).findFirst();
        if (entryOptional.isPresent()){
            ShoppingListEntry entry = entryOptional.get();
            this.entries.remove(entry);
            return entry;
        } else {
            throw new ProductDoesntExistException(product.getId());
        }
    }
}
