package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.MultipleDiscountWithSingleItemException;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    public BigDecimal calculateFor(List<ShoppingListEntry> entries) {
        BigDecimal total = new BigDecimal(0);
        boolean isValid = this.products.stream().allMatch(product -> this.mapListContains(product, entries));
        if (isValid) {
            for (ShoppingListEntry entry : entries) {
                Product product = entry.getProduct();
                if (this.findProduct(product).isPresent()) {
                    BigDecimal discount = BigDecimal.valueOf(this.percentage).multiply(BigDecimal.valueOf(0.01)).multiply(product.getPrice());
                    total = total.add(product.getPrice().subtract(discount).multiply(BigDecimal.valueOf(entry.getQuantity())));
                    if (entry.getQuantity() > 1) {
                        entry.setQuantity(entry.getQuantity() - 1);
                    } else {
                        entries.remove(entry);
                    }
                }
            }
        }
        return total;
    }

    private boolean mapListContains(Product product, List<ShoppingListEntry> entries) {
        for (ShoppingListEntry entry : entries) {
            Product key = entry.getProduct();
            if (key.getId().equals(product.getId())) {
                return true;
            }
        }
        return false;
    }
}
