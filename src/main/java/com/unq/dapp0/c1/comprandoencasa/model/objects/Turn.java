package com.unq.dapp0.c1.comprandoencasa.model.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Shop shop;

    @Column
    private LocalDateTime time;

    public Turn(){}

    public Turn(Shop shop, LocalDateTime time) {
        this.shop = shop;
        this.time = time;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Shop getShop() {
        return this.shop;
    }

    public LocalDateTime getTime() {
        return this.time;
    }
}
