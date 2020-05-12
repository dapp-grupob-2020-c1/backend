package com.unq.dapp0.c1.comprandoencasa.model;

import java.time.LocalDateTime;

public class Turn {
    private final Shop shop;
    private final LocalDateTime time;

    public Turn(Shop shop, LocalDateTime time) {
        this.shop = shop;
        this.time = time;
    }

    public Shop getShop() {
        return this.shop;
    }

    public LocalDateTime getTime() {
        return this.time;
    }
}
