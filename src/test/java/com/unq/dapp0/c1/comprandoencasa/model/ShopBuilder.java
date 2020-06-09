package com.unq.dapp0.c1.comprandoencasa.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;

public class ShopBuilder {
    private String name;
    private ArrayList<ShopCategory> shopCategories;
    private Location location;
    private ArrayList<DayOfWeek> days;
    private LocalTime openingHour;
    private LocalTime closingHour;
    private ArrayList<PaymentMethod> paymentMethods;
    private Integer deliveryRadius;
    private Manager manager;
    private ArrayList<Product> products;

    public static ShopBuilder anyShop(){
        return new ShopBuilder();
    }

    public ShopBuilder(){
        this.name = "test";
        this.shopCategories = new ArrayList<>();
        this.location = mock(Location.class);
        this.days = new ArrayList<>();
        this.openingHour = LocalTime.of(8,0);
        this.closingHour = LocalTime.of(17,0);
        this.paymentMethods = new ArrayList<>();
        this.deliveryRadius = 1;
        this.manager = mock(Manager.class);
        this.products = new ArrayList<>();
    }

    public Shop build(){
        return new Shop(
                name,
                shopCategories,
                location,
                days,
                openingHour,
                closingHour,
                paymentMethods,
                deliveryRadius,
                manager,
                products);
    }
    public ShopBuilder withCategories(ArrayList<ShopCategory> shopCategories){
        this.shopCategories = shopCategories;
        return this;
    }
    public ShopBuilder withLocation(Location location){
        this.location = location;
        return this;
    }
    public ShopBuilder withDays(ArrayList<DayOfWeek> days){
        this.days = days;
        return this;
    }
    public ShopBuilder withOpeningHour(LocalTime openingHour){
        this.openingHour = openingHour;
        return this;
    }
    public ShopBuilder withClosingHour(LocalTime closingHour){
        this.closingHour = closingHour;
        return this;
    }
    public ShopBuilder withPaymentMethods(ArrayList<PaymentMethod> paymentMethods){
        this.paymentMethods = paymentMethods;
        return this;
    }
    public ShopBuilder withDeliveryRadius(Integer deliveryRadius) {
        this.deliveryRadius = deliveryRadius;
        return this;
    }
    public ShopBuilder withManager(Manager manager) {
        this.manager = manager;
        return this;
    }

    public ShopBuilder withProducts(ArrayList<Product> products) {
        this.products = products;
        return this;
    }

    public ShopBuilder withName(String name) {
        this.name = name;
        return this;
    }
}
