package com.unq.dapp0.c1.comprandoencasa.model.objects;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.MultipleDiscountWithSingleItemException;
import com.unq.dapp0.c1.comprandoencasa.model.exceptions.ProductIsInvalidForDiscount;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity(name = "DiscountByMultiple")
public class DiscountByMultiple extends Discount {

    @ManyToMany
    private Collection<Product> products;

    public DiscountByMultiple(){}

    public DiscountByMultiple(double percentage, LocalDate startingDate, LocalDate endingDate, Shop shop, List<Product> products) {
        super(percentage, startingDate, endingDate, shop);
        this.products = products;
    }

    @Override
    public boolean isTypeMultiple() {
        return true;
    }

    public void setProducts(Collection<Product> products){this.products = products;}

    public Collection<Product> getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.findProduct(product).ifPresent(value -> {
            if (this.products.size() == 2) {
                throw new MultipleDiscountWithSingleItemException(product);
            } else {
                this.products.remove(value);
            }
        });
    }

    private Optional<Product> findProduct(Product product) {
        return this.products.stream().filter(p -> p.getId().equals(product.getId())).findFirst();
    }

    @Override
    public int compare(Discount discount) {
        if (this.percentage < discount.percentage) {
            return -1;
        } else if (discount.isTypeSingle() || discount.isTypeCategory() || this.percentage > discount.percentage) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public BigDecimal calculateFor(ShoppingListEntry entry, List<ShoppingListEntry> entries) {
        Product product = entry.getProduct();
        boolean isValid = mapListContains(entries);
        if (isValid){
            BigDecimal perc = BigDecimal.valueOf(this.percentage).multiply(BigDecimal.valueOf(0.01));
            BigDecimal finalPerc = BigDecimal.valueOf(1).subtract(perc);
            BigDecimal total = product.getPrice().multiply(finalPerc);
            return total;
        } else {
            throw new ProductIsInvalidForDiscount(product, this);
        }
    }

    @Override
    public boolean isValidFor(Product product, List<ShoppingListEntry> entries, int amountEvaluated) {
        List<ShoppingListEntry> matchingEntries = new ArrayList<>();
        for (Product prod : products){
            Optional<ShoppingListEntry> filterEntry = entries.stream()
                    .filter(entry -> entry.getProduct().getId().equals(prod.getId())).findFirst();
            if (filterEntry.isPresent()){
                matchingEntries.add(filterEntry.get());
            } else {
                return false;
            }
        }
        for (ShoppingListEntry entry : matchingEntries){
            if (entry.getQuantity() <= amountEvaluated){
                return false;
            }
        }
        return true;
    }

    private boolean mapListContains(List<ShoppingListEntry> entries) {
        boolean returnValue = true;
        for (Product prod : products){
            returnValue = returnValue && entries.stream()
                    .anyMatch(entry -> entry.getProduct().getId().equals(prod.getId()));
        }
        return returnValue;
    }
}
