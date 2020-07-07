package com.unq.dapp0.c1.comprandoencasa.model;

import javax.persistence.*;
import java.util.ArrayList;

@Entity(name = "DeliveryAtHome")
public class DeliveryAtHome extends ShopDelivery {

    @OneToOne
    private Location location;

    public DeliveryAtHome(){}

    public DeliveryAtHome(Shop shop, ArrayList<Product> products, User user, Location location) {
        super(shop, products, user);
        this.location = location;
    }
    public Location getLocation() {
        return this.location;
    }
}
