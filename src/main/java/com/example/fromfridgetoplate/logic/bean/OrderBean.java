package com.example.fromfridgetoplate.logic.bean;


import com.example.fromfridgetoplate.logic.model.CartItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderBean {
    private int orderId;
    private String customerId;

    private String shopId;
    private List<CartItem> cartItems; // food_item alias ingrediente, ancora da definire
    private LocalDateTime orderTime;
    private AddressBean shippingAddress;

    private String shippingCity;


    private String status;
    int riderId;


    public OrderBean(int orderId, AddressBean shippingAddress) {
        this.orderId = orderId;
        this.shippingAddress = shippingAddress;
    }

    public OrderBean(String shopId, AddressBean shippingAddress) {
        this.shopId = shopId;
        this.shippingAddress = shippingAddress;
    }

    public OrderBean( String customerId, int orderId, AddressBean shippingAddress) {
        this(orderId, shippingAddress);
        this.customerId = customerId;
    }
    public OrderBean( int riderId, int orderId, AddressBean shippingAddress) {
        this(orderId, shippingAddress);
        this.riderId = riderId;
    }


    public OrderBean(int orderId, String customerId, String shopId, String status, LocalDateTime orderTime, int riderId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shopId = shopId;
        this.status = status;
        this.orderTime = orderTime;
        this.riderId = riderId;

    }

    public OrderBean(int orderId, String customerId, String shopId, String status, LocalDateTime orderTime, int riderId, String city) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shopId = shopId;
        this.status = status;
        this.orderTime = orderTime;
        this.riderId = riderId;
        this.shippingCity = city;
    }

    public OrderBean(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setCustomerId(String customerId) {
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


    public int getRiderId() {
        return riderId;
    }

    public void setRiderId(int riderId) {
        this.riderId = riderId;
    }


}
