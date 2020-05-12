package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;
import java.util.*;

public class ShoppingList {

    private final Customer customer;
    // Map.Entry<Product, Integer> es un producto y su cantidad, dentro de una lista de compras
    // yo quería una tupla, pero... JAVA.
    private List<Map.Entry<Product, Integer>> entries;

    // ubicación hacia donde se debe hacer el envio, domicilio de comprador
    private Location location;

    public ShoppingList(Location location, Customer customer) {
        this.entries = new ArrayList<>();
        this.location = location;
        this.customer = customer;
    }

    public void add(Product aProduct, int aQuantity) {
        // TODO: chequeo errores, tiro exceptions

        // creo una nueva shoppingListEntry para agregar al listado products
        Map.Entry<Product, Integer> newShoppingListEntry = new AbstractMap.SimpleEntry<>(aProduct, aQuantity);
        entries.add(newShoppingListEntry);
    }

    public int countProducts() {
        return this.entries.size();
    }

    public int countItems() {
        int sum = 0;
        for (Map.Entry<Product, Integer> shoppingListEntry : entries) {
            sum += shoppingListEntry.getValue();
        }
        return sum;
    }

    public BigDecimal totalValue() {
        HashSet<Shop> shops = new HashSet<>();
        this.entries.forEach(el -> shops.add(el.getKey().getShop()));

        ArrayList<Discount> discounts = new ArrayList<>();
        shops.forEach(shop -> discounts.addAll(shop.getActiveDiscounts()));

        discounts.sort(Discount::compare);

        BigDecimal total = new BigDecimal(0);
        List<Map.Entry<Product, Integer>> products =  new ArrayList<>(this.entries);

        for (Discount discount : discounts){
            total = discount.calculateFor(products);
        }

        if (!products.isEmpty()){
            for (Map.Entry<Product, Integer> shoppingListEntry : products) {
                BigDecimal productCalculatedPrice = shoppingListEntry.getKey().getPrice();
                total = total.add(productCalculatedPrice.multiply(BigDecimal.valueOf(shoppingListEntry.getValue())));
            }
        }

        return total;
    }

    public List<Map.Entry<Product, Integer>> getEntries() {
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
        for (Map.Entry<Product, Integer> productIntegerEntry : this.entries){
            Product product = productIntegerEntry.getKey();
            if (product.isType(productType)){
                Integer amount = productIntegerEntry.getValue();
                total = total.add(product.getPrice().multiply(BigDecimal.valueOf(amount)));
            }
        }
        return total;
    }
}
