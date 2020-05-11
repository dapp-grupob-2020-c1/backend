package com.unq.dapp0.c1.comprandoencasa.model;

import java.time.LocalDate;

public class Discount {
    private final long id;
    private LocalDate startingDate;
    private LocalDate endingDate;
    private double percentage;

    public Discount(long id, double percentage, LocalDate startingDate, LocalDate endingDate) {
        this.checkDates(startingDate, endingDate);

        this.id = id;
        this.percentage = percentage;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
    }

    private void checkDates(LocalDate startingDate, LocalDate endingDate) {
        if (startingDate.isAfter(endingDate))
            throw new InvalidDiscountDatesException(startingDate, endingDate);
    }

    public Long getID() {
        return this.id;
    }

    public Boolean isActive() {
        return null;
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
}
