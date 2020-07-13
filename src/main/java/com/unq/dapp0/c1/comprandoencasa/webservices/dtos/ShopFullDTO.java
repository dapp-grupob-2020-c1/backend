package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.PaymentMethod;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.ShopCategory;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class ShopFullDTO {
    public List<PaymentMethod> paymentMethods;
    public Long id;
    public String name;
    public List<ShopCategory> categories;
    public Location location;
    public List<DayOfWeek> days;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    public LocalTime openingHour;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    public LocalTime closingHour;
    public Integer deliveryRadius;
    public List<ProductSmallDTO> products;
    public List<DiscountDTO> discounts;
    public List<ShopDeliveryDTO> orders;

    public ShopFullDTO(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.categories = shop.getShopCategories();
        this.location = shop.getLocation();
        this.days = shop.getDays();
        this.openingHour = shop.getOpeningHour();
        this.closingHour = shop.getClosingHour();
        this.deliveryRadius = shop.getDeliveryRadius();
        this.products = ProductSmallDTO.createProducts(shop.getProducts());
        this.discounts = DiscountDTO.createDiscounts(shop.getActiveDiscounts());
        this.orders = ShopDeliveryDTO.createDeliveries(shop.getActiveDeliveries());
        this.paymentMethods = shop.getPaymentMethods();
    }
}
