package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.PaymentMethod;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopCategory;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ShopSmallDTO {
    public String imageUrl;
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
    public List<DiscountDTO> discounts;
    public List<PaymentMethod> paymentMethods;

    public ShopSmallDTO(){}

    public ShopSmallDTO(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.imageUrl = shop.getImageUrl();
        this.categories = shop.getShopCategories();
        this.location = shop.getLocation();
        this.days = shop.getDays();
        this.openingHour = shop.getOpeningHour();
        this.closingHour = shop.getClosingHour();
        this.deliveryRadius = shop.getDeliveryRadius();
        this.discounts = DiscountDTO.createDiscounts(shop.getActiveDiscounts());
        this.paymentMethods = shop.getPaymentMethods();
    }

    public static List<ShopSmallDTO> generateShopSmallDTOList(List<Shop> shops) {
        List<ShopSmallDTO> returnList = new ArrayList<>();
        for (Shop shop : shops){
            returnList.add(new ShopSmallDTO(shop));
        }
        return returnList;
    }
}
