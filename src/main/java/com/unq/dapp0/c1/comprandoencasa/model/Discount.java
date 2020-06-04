package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.exceptions.InvalidDiscountDatesException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
public abstract class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected final long id;

    @Column
    protected LocalDate startingDate;

    @Column
    protected LocalDate endingDate;

    @Column
    protected double percentage;

    @OneToOne
    protected final Shop shop;

    protected Discount(long id, double percentage, LocalDate startingDate, LocalDate endingDate, Shop shop){
        this.checkDates(startingDate, endingDate);

        this.id = id;
        this.percentage = percentage;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.shop = shop;
    }

    private void checkDates(LocalDate startingDate, LocalDate endingDate) {
        if (startingDate.isAfter(endingDate))
            throw new InvalidDiscountDatesException(startingDate, endingDate);
    }

    public Long getID() {
        return this.id;
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

    public boolean isTypeCategory(){
        return false;
    }

    public boolean isTypeSingle(){
        return false;
    }

    public boolean isTypeMultiple(){
        return false;
    }

    public abstract int compare(Discount discount);

    public BigDecimal calculateFor(List<ShoppingListEntry> entries){
        return null;
    }
}
