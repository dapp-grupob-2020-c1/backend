package com.unq.dapp0.c1.comprandoencasa.model.objects;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.ProductIsInvalidForDiscount;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "DiscountByCategory")
public class DiscountByCategory extends Discount {

    @Column
    private ProductType productType;

    public DiscountByCategory(){}

    public DiscountByCategory(double percentage, LocalDate startingDate, LocalDate endingDate, Shop shop, ProductType productType) {
        super(percentage, startingDate, endingDate, shop);
        this.productType = productType;
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public ArrayList<Product> getProducts() {
        return this.shop.getProducts().stream()
                .filter(p -> p.isType(this.productType))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public boolean isTypeCategory() {
        return true;
    }

    @Override
    public int compare(Discount discount) {
        if (discount.isTypeSingle() || discount.isTypeMultiple() || this.percentage < discount.percentage) {
            return -1;
        } else if (this.percentage > discount.percentage) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public BigDecimal calculateFor(ShoppingListEntry entry, List<ShoppingListEntry> entries) {
        Product product = entry.getProduct();
        if (product.isType(productType)){
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
        return product.isType(productType);
    }
}
