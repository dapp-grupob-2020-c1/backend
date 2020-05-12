package com.unq.dapp0.c1.comprandoencasa.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class TurnTests {

    @Test
    public void aTurnHasAShop(){
        Shop shop = mock(Shop.class);

        Turn turn = TurnBuilder.anyTurn()
                .withShop(shop)
                .build();

        assertEquals(shop, turn.getShop());
    }

    @Test
    public void aTurnHasAnExactDate(){
        LocalDateTime dateTime = LocalDateTime.now();

        Turn turn = TurnBuilder.anyTurn()
                .withTime(dateTime)
                .build();

        assertEquals(dateTime, turn.getTime());
    }

}

class TurnBuilder{

    private Shop shop;
    private LocalDateTime time;

    public static TurnBuilder anyTurn() {
        return new TurnBuilder();
    }

    private TurnBuilder(){
        this.shop = mock(Shop.class);
        this.time = LocalDateTime.now();
    }

    public Turn build() {
        return new Turn(shop, time);
    }

    public TurnBuilder withShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public TurnBuilder withTime(LocalDateTime dateTime) {
        this.time = dateTime;
        return this;
    }
}