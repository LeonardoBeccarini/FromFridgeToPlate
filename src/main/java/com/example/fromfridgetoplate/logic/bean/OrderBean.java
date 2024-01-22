package com.example.fromfridgetoplate.logic.bean;


import com.example.fromfridgetoplate.logic.model.Food_item;

import java.time.LocalDateTime;
import java.util.List;

public class OrderBean {
    private int orderId;
    private int customerId;
    private List<Food_item> foodItems; // food_item alias ingrediente, ancora da definire
    private LocalDateTime orderTime;
    private AddressBean shippingAddress;

    private String shippingCity;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderBean() {

    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Food_item> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<Food_item> foodItems) {
        this.foodItems = foodItems;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public AddressBean getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressBean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }
}
