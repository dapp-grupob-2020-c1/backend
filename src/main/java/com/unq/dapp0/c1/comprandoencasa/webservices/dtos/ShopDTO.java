package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.ShopCategory;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class ShopDTO {
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

    public ShopDTO(){}

    public ShopDTO(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.categories = shop.getShopCategories();
        this.location = shop.getLocation();
        this.days = shop.getDays();
        this.openingHour = shop.getOpeningHour();
        this.closingHour = shop.getClosingHour();
        this.deliveryRadius = shop.getDeliveryRadius();
    }
}
