package com.example.fromfridgetoplate.logic.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Order implements Serializable {

    private int orderId;

    private String customerId;

    private String shopId;

    private int riderId;

    private String status;

    private List<CartItem> cartItems; // Una lista degli ingredienti alimentari ordinati

    private LocalDateTime orderTime;

    private LocalDateTime deliveryTime;

    private boolean isAcceptedByRider;

    private String shippingStreet;
    private int shippingStreetNumber;

    private String shippingCity;
    private String shippingProvince;



    public Order(int orderId, String customerId, String shopId, String status, List<CartItem> cartItems, LocalDateTime orderTime) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shopId = shopId;
        this.status = status;
        this.cartItems = cartItems;
        this.orderTime = orderTime;
    }

    public Order(int orderId, String customerId, String shopId, String status, LocalDateTime orderTime, int riderId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.shopId = shopId;
        this.status = status;
        this.orderTime = orderTime;
        this.riderId = riderId;
    }

    public Order(int orderId, String shippingStreet, int shippingStreetNumber, String shippingCity, String shippingProvince) {
        this.orderId = orderId;
        this.shippingStreet = shippingStreet;
        this.shippingStreetNumber = shippingStreetNumber;
        this.shippingCity = shippingCity;
        this.shippingProvince = shippingProvince;
    }

    public Order(int orderId) {
        this.orderId = orderId;
    }

    public Order(int orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public Order(String shopId, String customerId, String shippingStreet, int shippingStreetNumber, String shippingCity, String shippingProvince) {
        this.shopId = shopId;
        this.customerId = customerId;
        this.shippingStreet = shippingStreet;
        this.shippingStreetNumber = shippingStreetNumber;
        this.shippingCity = shippingCity;
        this.shippingProvince = shippingProvince;
    }

    // Metodi getter e setter
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }



    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getRiderId() {
        return riderId;
    }

    public void setRiderId(int riderId) {
        this.riderId = riderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CartItem> getItems() {
        return cartItems;
    }

    public void setItems(List<CartItem> items) {
        this.cartItems = items;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public boolean isAcceptedByRider() {
        return isAcceptedByRider;
    }

    public void setAcceptedByRider(boolean acceptedByRider) {
        isAcceptedByRider = acceptedByRider;
    }

    public String getShippingStreet() {
        return shippingStreet;
    }

    public void setShippingStreet(String shippingStreet) {
        this.shippingStreet = shippingStreet;
    }

    public int getShippingStreetNumber() {
        return shippingStreetNumber;
    }

    public void setShippingStreetNumber(int shippingStreetNumber) {
        this.shippingStreetNumber = shippingStreetNumber;
    }

    public String getShippingProvince() {
        return shippingProvince;
    }

    public void setShippingProvince(String shippingProvince) {
        this.shippingProvince = shippingProvince;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }


    // Altri metodi, come per accettare l'ordine da parte del rider
    public void acceptOrderByRider(int riderId) {
        this.riderId = riderId;
        this.isAcceptedByRider = true;
        this.status = "in consegna";
    }



    // Metodo per aggiornare lo stato dell'ordine
    public void updateStatus(String status) {
        this.status = status;
    }



    // Esempio di metodo per calcolare il tempo stimato di consegna
    public void estimateDeliveryTime() {
        // da finire
    }

    public void setIsAcceptedByRider(boolean isAcceptedByRider) {
        // da finire
    }




}
