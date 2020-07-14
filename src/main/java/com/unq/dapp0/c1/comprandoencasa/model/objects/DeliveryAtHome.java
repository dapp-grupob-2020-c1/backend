package com.unq.dapp0.c1.comprandoencasa.model.objects;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@DiscriminatorValue("B")
public class DeliveryAtHome extends ShopDelivery {

    @OneToOne
    private Location location;

    @Column
    private LocalDateTime dateOfDelivery;

    public DeliveryAtHome(){}

    public DeliveryAtHome(Shop shop, ArrayList<Product> products, User user, Location location, LocalDateTime dateOfDelivery) {
        super(shop, products, user);
        this.location = location;
        this.dateOfDelivery = dateOfDelivery;
    }
    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location){this.location = location;}

    public LocalDateTime getDateOfDelivery(){return this.dateOfDelivery;}

    public void setDateOfDelivery(LocalDateTime dateOfDelivery){this.dateOfDelivery = dateOfDelivery;}
}
