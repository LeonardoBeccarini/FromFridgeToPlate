package com.example.fromfridgetoplate.logic.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private int orderId;

    private int customerId;

    private String retailerId;

    private int riderId;

    private String status; // "Pending", "Accepted", "Delivered"........altri? dovrebbe essere un enum

    private List<CartItem> cartItems; // Una lista degli ingredienti alimentari ordinati

    private LocalDateTime orderTime;

    private LocalDateTime deliveryTime;

    private boolean isAcceptedByRider;

    private String shippingStreet;
    private int shippingStreetNumber;

    private String shippingCity;
    private String shippingProvince;



    public Order(int orderId, int customerId, String retailerId, String status, List<CartItem> cartItems, LocalDateTime orderTime) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.retailerId = retailerId;
        this.status = status;
        this.cartItems = cartItems;
        this.orderTime = orderTime;
    }

    public Order(int orderId, int customerId, String retailerId, String status, LocalDateTime orderTime, int riderId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.retailerId = retailerId;
        this.status = status;
        this.orderTime = orderTime;
        this.riderId = riderId;
    }


    // Metodi getter e setter
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

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
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


    /** questi metodi che seguono son un pò dubbi, sopratutto acceptOrderByRider dovrebbe prendersi come paramentro l'entitò Rider e non solo l'id**/

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
