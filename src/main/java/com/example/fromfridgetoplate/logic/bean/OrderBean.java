package com.example.fromfridgetoplate.logic.bean;


import com.example.fromfridgetoplate.logic.model.CartItem;
import com.example.fromfridgetoplate.logic.model.FoodItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderBean {
    private int orderId;
    private int customerId;
    private List<CartItem> cartItems; // food_item alias ingrediente, ancora da definire
    private LocalDateTime orderTime;
    private AddressBean shippingAddress;

    private String shippingCity;

    private String status;
    int riderId;
    String retailerId;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderBean(int orderId, int customerId, String retailerId, String status, LocalDateTime orderTime, int riderId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.retailerId = retailerId;
        this.status = status;
        this.orderTime = orderTime;
        this.riderId = riderId;
    }
    public OrderBean(){}


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

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setFoodItems(List<CartItem> foodItems) {
        this.cartItems = foodItems;
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
