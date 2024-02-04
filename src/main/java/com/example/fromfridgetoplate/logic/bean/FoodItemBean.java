package com.example.fromfridgetoplate.logic.bean;

public class FoodItemBean {
    private String name;
    private float price;

    public FoodItemBean(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public FoodItemBean(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }
}
