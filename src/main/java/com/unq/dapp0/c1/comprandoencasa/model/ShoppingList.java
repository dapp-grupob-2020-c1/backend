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

@Entity
@Table
public class ShoppingList {

    @OneToOne
    private final Customer customer;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Map.Entry<Product, Integer> es un producto y su cantidad, dentro de una lista de compras
    // yo quería una tupla, pero... JAVA.
    @OneToMany
    private final List<ShoppingListEntry> entries;

    // ubicación hacia donde se debe hacer el envio, domicilio de comprador
    @OneToOne
    private Location location;

    public ShoppingList(Location location, Customer customer) {
        this.entries = new ArrayList<>();
        this.location = location;
        this.customer = customer;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void add(Product aProduct, int aQuantity) {
        // TODO: chequeo errores, tiro exceptions

        ShoppingListEntry newEntry = new ShoppingListEntry(aProduct, aQuantity);
        entries.add(newEntry);
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

    public List<ShoppingListEntry> getEntries() {
        return this.entries;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location aLocation) {
        this.location = aLocation;
    }

    public Customer getCustomer() {
        return this.customer;
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
