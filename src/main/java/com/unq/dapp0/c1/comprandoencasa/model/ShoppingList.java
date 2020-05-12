package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingList {

    // Map.Entry<Product, Integer> es un producto y su cantidad, dentro de una lista de compras
    // yo quería una tupla, pero... JAVA.
    private List<Map.Entry<Product, Integer>> products;

    public ShoppingList() {
        this.products = new ArrayList<>();
    }

    public void add(Product aProduct, int aQuantity) {
        // TODO: chequeo errores, tiro exceptions

        // creo una nueva shoppingListEntry para agregar al listado products
        Map.Entry<Product, Integer> newShoppingListEntry = new AbstractMap.SimpleEntry<>(aProduct, aQuantity);
        products.add(newShoppingListEntry);
    }

    public int countProducts() {
        return this.products.size();
    }

    public int countItems() {
        int sum = 0;
        for (Map.Entry<Product, Integer> shoppingListEntry : products) {
            sum += shoppingListEntry.getValue();
        }
        return sum;
    }

    public BigDecimal totalValue() {
        BigDecimal total = new BigDecimal(0);
        for (Map.Entry<Product, Integer> shoppingListEntry : products) {
            BigDecimal productCalculatedPrice = shoppingListEntry.getKey().getTotalPrice(this.products);
            total = total.add(productCalculatedPrice);
        }
        return total;
    }
}
