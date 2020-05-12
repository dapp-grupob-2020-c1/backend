package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

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
        Product product = mock(Product.class);

        ArrayList<Product> products = new ArrayList<>();
        products.add(product);

        ShopDelivery delivery = ShopDeliveryBuilder.anyDelivery()
                .withProducts(products)
                .build();

        assertTrue(delivery.getProducts().contains(product));
    }

    @Test
    public void aDeliveryCanBeOfACustomer(){
        Customer customer = mock(Customer.class);

        ShopDelivery delivery = ShopDeliveryBuilder.anyDelivery()
                .withCustomer(customer)
                .build();

        assertEquals(customer, delivery.getCustomer());
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
    private ArrayList<Product> products;
    private Customer customer;
    private Turn turn;
    private Location location;

    public static ShopDeliveryBuilder anyDelivery() {
        return new ShopDeliveryBuilder();
    }

    private ShopDeliveryBuilder(){
        this.shop = mock(Shop.class);
        this.products = new ArrayList<>();
        this.customer = mock(Customer.class);
        this.turn = mock(Turn.class);
    }

    public ShopDelivery build() {
        if (this.turn != null){
            return new DeliveryAtShop(shop, products, customer, turn);
        } else {
            return new DeliveryAtHome(shop, products, customer, location);
        }
    }

    public ShopDeliveryBuilder withShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public ShopDeliveryBuilder withProducts(ArrayList<Product> products) {
        this.products = products;
        return this;
    }

    public ShopDeliveryBuilder withCustomer(Customer customer) {
        this.customer = customer;
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