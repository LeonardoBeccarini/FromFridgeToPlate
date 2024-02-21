package com.example.fromfridgetoplate.logic.bean;

public class NotificationBean {

    private int notificationId;
    private int riderId;

    private String customer;
    private int orderId;
    private String street;
    private int streetNumber;
    private String city;
    private String province;
    private String messageText;

    public NotificationBean(int riderId, int orderId, String street, int streetNumber, String city, String province, String messageText) {
        this.riderId = riderId;
        this.orderId = orderId;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.province = province;
        this.messageText = messageText;
    }

    public NotificationBean(String customer, int orderId, String street, int streetNumber, String city, String province, String messageText) {
        this.customer = customer;
        this.orderId = orderId;
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.province = province;
        this.messageText = messageText;
    }

    public String getCustomer() {
        return customer;
    }

    public int getRiderId() {
        return riderId;
    }

    public void setRiderId(int riderId) {
        this.riderId = riderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}

