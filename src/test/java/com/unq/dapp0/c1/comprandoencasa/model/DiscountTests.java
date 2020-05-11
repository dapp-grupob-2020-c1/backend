package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiscountTests {
    @Test
    public void aDiscountHasAnID(){
        Discount discount = DiscountBuilder.anyDiscount().withID(1L).build();

        assertEquals(1L, discount.getID());
    }

    @Test
    public void aDiscountCanHaveADiscountPercentage(){
        Discount discount = DiscountBuilder.anyDiscount().withPercentage(1.0).build();

        assertEquals(1.0, discount.getPercentage());
    }

    @Test
    public void aDiscountCanChangeItsDiscountPercentage(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        discount.setPercentage(2.0);

        assertEquals(2.0, discount.getPercentage());
    }

    @Test
    public void aDiscountHasAStartingDate(){
        LocalDate startingDate = LocalDate.now();

        Discount discount = DiscountBuilder.anyDiscount().setStartingDate(startingDate).build();

        assertEquals(startingDate, discount.getStartingDate());
    }

    @Test
    public void aDiscountHasAnEndingDate(){
        LocalDate endingDate = LocalDate.now();

        Discount discount = DiscountBuilder.anyDiscount().setEndingDate(endingDate).build();

        assertEquals(endingDate, discount.getEndingDate());
    }

    @Test
    public void aDiscountCannotHaveAnEndingDateThatIsBeforeTheStartingDateAndViceversa(){
        LocalDate startingDate = LocalDate.of(2020, 5, 2);
        LocalDate endingDate = LocalDate.of(2020, 5, 1);

        Throwable exception = assertThrows(InvalidDiscountDatesException.class, ()->DiscountBuilder.anyDiscount()
                .setStartingDate(startingDate)
                .setEndingDate(endingDate)
                .build());

        assertEquals("The dates given for the discount are invalid. Starting date is "+startingDate.toString()+" and ending date is "+endingDate.toString(),
                exception.getMessage());
    }

    @Test
    public void aDiscountCanChangeItsStartingDate(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        LocalDate newStartingDate = LocalDate.of(2020, 1, 1);

        discount.setStartingDate(newStartingDate);

        assertEquals(newStartingDate, discount.getStartingDate());
    }

    @Test
    public void aDiscountCannotChangeItsStartingDateToADateAfterItsEndingDate(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        LocalDate newStartingDate = LocalDate.of(2021, 1, 1);

        Throwable exception = assertThrows(InvalidDiscountDatesException.class, ()->
                discount.setStartingDate(newStartingDate));

        assertEquals("The dates given for the discount are invalid. Starting date is "+newStartingDate.toString()+" and ending date is "+LocalDate.now().toString(),
                exception.getMessage());
    }

    @Test
    public void aDiscountCanChangeItsEndingDate(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        LocalDate newEndingDate = LocalDate.of(2021, 1, 1);

        discount.setEndingDate(newEndingDate);

        assertEquals(newEndingDate, discount.getEndingDate());
    }

    @Test
    public void aDiscountCannotChangeItsEndingDateToADateBeforeTheStartingDate(){
        Discount discount = DiscountBuilder.anyDiscount().build();

        LocalDate newEndingDate = LocalDate.of(2020, 1, 1);

        Throwable exception = assertThrows(InvalidDiscountDatesException.class, ()->
                discount.setEndingDate(newEndingDate));

        assertEquals("The dates given for the discount are invalid. Starting date is "+LocalDate.now().toString()+" and ending date is "+newEndingDate.toString(),
                exception.getMessage());
    }

    @Test
    public void aDiscountCanBeForAProductType(){

    }

    @Test
    public void aDiscountCanBeForASpecificProduct(){

    }

    @Test
    public void aDiscountCanBeForMultipleProducts(){

    }
}

class DiscountBuilder{
    private long id;
    private double percentage;
    private LocalDate startingDate;
    private LocalDate endingDate;

    public static DiscountBuilder anyDiscount(){
        return new DiscountBuilder();
    }

    private DiscountBuilder(){
        this.id = 1L;
        this.percentage = 1.0;
        this.startingDate = LocalDate.now();
        this.endingDate = LocalDate.now();
    }

    public Discount build(){
        return new Discount(id, percentage, startingDate, endingDate);
    }

    public DiscountBuilder withID(long id) {
        this.id = id;
        return this;
    }

    public DiscountBuilder withPercentage(double percentage) {
        this.percentage = percentage;
        return this;
    }

    public DiscountBuilder setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
        return this;
    }

    public DiscountBuilder setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
        return this;
    }
}