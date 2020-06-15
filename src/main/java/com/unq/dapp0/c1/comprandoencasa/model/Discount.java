package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidDiscountDatesException;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Discount {

    @OneToOne
    protected Shop shop;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected long id;
    @Column
    protected LocalDate startingDate;
    @Column
    protected LocalDate endingDate;
    @Column
    protected double percentage;

    protected Discount(){}

    protected Discount(double percentage, LocalDate startingDate, LocalDate endingDate, Shop shop) {
        this.checkDates(startingDate, endingDate);

        this.percentage = percentage;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.shop = shop;
    }

    private void checkDates(LocalDate startingDate, LocalDate endingDate) {
        if (startingDate.isAfter(endingDate))
            throw new InvalidDiscountDatesException(startingDate, endingDate);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActive() {
        LocalDate now = LocalDate.now();
        return (this.startingDate.isBefore(now) || this.startingDate.isEqual(now)) &&
                (this.endingDate.isAfter(now) || this.endingDate.isEqual(now));
    }

    public double getPercentage() {
        return this.percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public LocalDate getStartingDate() {
        return this.startingDate;
    }

    public void setStartingDate(LocalDate newStartingDate) {
        this.checkDates(newStartingDate, this.endingDate);
        this.startingDate = newStartingDate;
    }

    public LocalDate getEndingDate() {
        return this.endingDate;
    }

    public void setEndingDate(LocalDate newEndingDate) {
        this.checkDates(this.startingDate, newEndingDate);
        this.endingDate = newEndingDate;
    }

    public Shop getShop() {
        return this.shop;
    }

    public boolean isTypeCategory() {
        return false;
    }

    public boolean isTypeSingle() {
        return false;
    }

    public boolean isTypeMultiple() {
        return false;
    }

    public abstract int compare(Discount discount);

    public BigDecimal calculateFor(List<ShoppingListEntry> entries) {
        return null;
    }
}
