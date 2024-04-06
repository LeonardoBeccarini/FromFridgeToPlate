

package com.example.fromfridgetoplate.logic.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String name;
    private float price;
    private double quantity;

    public CartItem(String name, double quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public CartItem(String name, float price, double quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}
