package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.PaymentMethod;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Product;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopCategory;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;

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
    private User user;
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
        this.user = mock(User.class);
        this.products = new ArrayList<>();
    }

    public Shop build(){
        Shop shop = new Shop(
                name,
                shopCategories,
                location,
                days,
                openingHour,
                closingHour,
                paymentMethods,
                deliveryRadius
        );
        products.forEach(shop::addProduct);
        shop.setUser(user);
        return shop;
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
    public ShopBuilder withUser(User user) {
        this.user = user;
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
