package com.unq.dapp0.c1.comprandoencasa.model.objects;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.ArrayList;

@Entity(name = "DeliveryAtShop")
public class DeliveryAtShop extends ShopDelivery {

    @OneToOne
    private Turn turn;

    public DeliveryAtShop(){}

    public DeliveryAtShop(Shop shop, ArrayList<Product> products, User customer, Turn turn) {
        super(shop, products, customer);
        this.turn = turn;
    }

    public Turn getTurn() {
        return this.turn;
    }

    public void setTurn(Turn turn){this.turn = turn;}
}
