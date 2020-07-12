package com.unq.dapp0.c1.comprandoencasa.webservices.dtos;

import com.unq.dapp0.c1.comprandoencasa.model.DeliveryAtHome;
import com.unq.dapp0.c1.comprandoencasa.model.DeliveryAtShop;
import com.unq.dapp0.c1.comprandoencasa.model.Location;
import com.unq.dapp0.c1.comprandoencasa.model.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.model.Turn;
import jdk.internal.jline.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShopDeliveryDTO {
    public Long id;
    public Long shopId;
    public List<Long> products;
    public UserPublicDTO user;
    @Nullable
    public Turn turn;
    @Nullable
    public Location location;

    public ShopDeliveryDTO(ShopDelivery delivery) {
        this.id = delivery.getId();
        this.shopId = delivery.getShop().getId();
        this.products = ProductSmallDTO.createProductsById(delivery.getProducts());
        this.user = new UserPublicDTO(delivery.getUser());
        if (delivery instanceof DeliveryAtShop){
            this.turn = ((DeliveryAtShop) delivery).getTurn();
        } else {
            this.location = ((DeliveryAtHome) delivery).getLocation();
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
