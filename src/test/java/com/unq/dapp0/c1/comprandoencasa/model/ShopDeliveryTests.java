package com.unq.dapp0.c1.comprandoencasa.model;

import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtHome;
import com.unq.dapp0.c1.comprandoencasa.model.objects.DeliveryAtShop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Location;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Shop;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShopDelivery;
import com.unq.dapp0.c1.comprandoencasa.model.objects.ShoppingListEntry;
import com.unq.dapp0.c1.comprandoencasa.model.objects.Turn;
import com.unq.dapp0.c1.comprandoencasa.model.objects.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ShopDeliveryTests {

    @Test
    public void aDeliveryHasAShopItBelongsTo(){
        Shop shop = mock(Shop.class);

        ShopDelivery delivery = ShopDeliveryBuilder.anyDelivery()
                .withShop(shop)
                .build();

        assertEquals(shop, delivery.getShop());
    }

    @Test
    public void aDeliveryHasAListOfProducts(){
        ShoppingListEntry product = mock(ShoppingListEntry.class);

        ArrayList<ShoppingListEntry> products = new ArrayList<>();
        products.add(product);

        ShopDelivery delivery = ShopDeliveryBuilder.anyDelivery()
                .withProducts(products)
                .build();

        assertTrue(delivery.getProducts().contains(product));
    }

    @Test
    public void aDeliveryCanBeOfAUser(){
        User user = mock(User.class);

        ShopDelivery delivery = ShopDeliveryBuilder.anyDelivery()
                .withUser(user)
                .build();

        assertEquals(user, delivery.getUser());
    }

    @Test
    public void aDeliveryCanBeADeliveryAtShopWithATurn(){
        Turn turn = mock(Turn.class);

        DeliveryAtShop delivery = (DeliveryAtShop) ShopDeliveryBuilder.anyDelivery()
                .withTurn(turn)
                .build();

        assertEquals(turn, delivery.getTurn());
    }

    @Test
    public void aDeliveryCanBeADeliveryAtHomeWithALocation(){
        Location location = mock(Location.class);

        DeliveryAtHome delivery = (DeliveryAtHome) ShopDeliveryBuilder.anyDelivery()
                .withLocation(location)
                .build();

        assertEquals(location, delivery.getLocation());
    }

}

class ShopDeliveryBuilder{
    private Shop shop;
    private ArrayList<ShoppingListEntry> products;
    private User user;
    private Turn turn;
    private Location location;
    private LocalDateTime time;

    public static ShopDeliveryBuilder anyDelivery() {
        return new ShopDeliveryBuilder();
    }

    private ShopDeliveryBuilder(){
        this.shop = mock(Shop.class);
        this.products = new ArrayList<>();
        this.user = mock(User.class);
        this.turn = mock(Turn.class);
        this.time = LocalDateTime.now();
    }

    public ShopDelivery build() {
        if (this.turn != null){
            return new DeliveryAtShop(shop, products, user, turn);
        } else {
            return new DeliveryAtHome(shop, products, user, location, time);
        }
    }

    public ShopDeliveryBuilder withShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public ShopDeliveryBuilder withProducts(ArrayList<ShoppingListEntry> products) {
        this.products = products;
        return this;
    }

    public ShopDeliveryBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public ShopDeliveryBuilder withTurn(Turn turn) {
        this.turn = turn;
        this.location = null;
        return this;
    }

    public ShopDeliveryBuilder withLocation(Location location) {
        this.location = location;
        this.turn = null;
        return this;
    }
}