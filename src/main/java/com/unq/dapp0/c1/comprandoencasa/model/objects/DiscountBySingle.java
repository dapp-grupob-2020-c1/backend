package com.unq.dapp0.c1.comprandoencasa.model.objects;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.ProductIsInvalidForDiscount;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "DiscountBySingle")
public class DiscountBySingle extends Discount {

    @OneToOne
    private Product product;

    public DiscountBySingle(){}

    public DiscountBySingle(double percentage, LocalDate startingDate, LocalDate endingDate, Shop shop, Product product) {
        super(percentage, startingDate, endingDate, shop);
        this.product = product;
    }

    @Override
    public boolean isTypeSingle() {
        return true;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int compare(Discount discount) {
        if (discount.isTypeMultiple() || this.percentage < discount.percentage) {
            return -1;
        } else if (discount.isTypeCategory() || this.percentage > discount.percentage) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public BigDecimal calculateFor(ShoppingListEntry entry, List<ShoppingListEntry> entries) {
        Product product = entry.getProduct();
        if (product.getId().equals(this.product.getId())){
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
        return product.getId().equals(this.product.getId());
    }
}
