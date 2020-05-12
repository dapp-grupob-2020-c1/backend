package com.unq.dapp0.c1.comprandoencasa.model;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingList {

    // Map.Entry<Product, Integer> es un producto y su cantidad, dentro de una lista de compras
    // yo quería una tupla, pero... JAVA.
    List<Map.Entry<Product, Integer>> products;

    public ShoppingList() {
        this.products = new ArrayList<>();
    }

    public int countProducts() {
        return this.products.size();
    }

    public void add(Product aProduct, int aQuantity) {
        // TODO: chequeo errores, tiro exceptions

        // creo una nueva instancia para agregar a la lista
        Map.Entry<Product, Integer> newShoppingListEntry = new AbstractMap.SimpleEntry<>(aProduct, aQuantity);
        products.add(newShoppingListEntry);
    }

    public int countItems() {
        int sum = 0;
        for (Map.Entry<Product, Integer> shoppingListEntry : products) {
            sum += shoppingListEntry.getValue();;
        }
        return sum;
    }

    public BigDecimal totalValue() {
        BigDecimal total = new BigDecimal(0);
        for (Map.Entry<Product, Integer> shoppingListEntry : products) {
            BigDecimal productPrice = shoppingListEntry.getKey().getPrice();
            BigDecimal productQuantity = new BigDecimal(shoppingListEntry.getValue());
            BigDecimal entryTotalPrice = productPrice.multiply(productQuantity);
            total = total.add(entryTotalPrice);
        }
        return total;
    }
}
