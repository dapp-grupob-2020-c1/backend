package com.unq.dapp0.c1.comprandoencasa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class ShoppingListEntry {

    @Id
    private Long id;

    @OneToOne
    private Product product;

    @Column
    private Integer quantity;

    public ShoppingListEntry(Product aProduct, Integer aQuantity) {
        this.product = aProduct;
        this.quantity = aQuantity;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}
