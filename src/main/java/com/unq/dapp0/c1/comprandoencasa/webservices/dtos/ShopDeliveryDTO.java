package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtHome;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtShop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Turn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShopDeliveryDTO {
    public Long id;
    public Long shopId;
    public List<Long> productEntries;
    public UserPublicDTO user;
    public Turn turn;
    public Location location;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime dateOfDelivery;

    public ShopDeliveryDTO(ShopDelivery delivery) {
        this.id = delivery.getId();
        this.shopId = delivery.getShop().getId();
        this.productEntries = ProductSmallDTO.createProductsById(delivery.getProducts());
        this.user = new UserPublicDTO(delivery.getUser());
        if (delivery instanceof DeliveryAtShop){
            this.turn = ((DeliveryAtShop) delivery).getTurn();
        } else {
            this.location = ((DeliveryAtHome) delivery).getLocation();
            this.dateOfDelivery = ((DeliveryAtHome) delivery).getDateOfDelivery();
        }
    }

    public static List<ShopDeliveryDTO> createDeliveries(List<ShopDelivery> deliveries) {
        List<ShopDeliveryDTO> returnList = new ArrayList<>();
        for (ShopDelivery delivery : deliveries){
            returnList.add(new ShopDeliveryDTO(delivery));
        }
        return returnList;
    }
}
