package com.example.fromfridgetoplate.logic.bean;

public class CartItemBean {
    private String name;
    private float price;
    private final double quantity;

    public CartItemBean(String name, float price, double quantity) {
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

}
