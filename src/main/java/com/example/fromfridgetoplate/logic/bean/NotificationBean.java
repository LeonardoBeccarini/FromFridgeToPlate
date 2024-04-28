package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.logic.model.Order;

public class NotificationBean {

    private int notificationId;
    private OrderBean orderBean;

    private String messageText;


    public NotificationBean(OrderBean orderBean, String messageText) {
        this.orderBean = orderBean;
        this.messageText = messageText;
    }


    public OrderBean getOrderBean() {
        return orderBean;
    }

    public void setOrderBean(OrderBean orderBean) {
        this.orderBean = orderBean;
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

    public int getOrderId () { // questo sarà chiamato dalla setcellValueFactory(), faccio cosi il metodo per il principio di demetra
        return orderBean.getOrderId();
    }

    private AddressBean getAddress(){
        return orderBean.getShippingAddress();
    }

    public String getStreet() {
        return getAddress().getShippingStreet();
    }

    public String getCity(){
        return getAddress().getShippingCity();
    }

    public int getStreetNumber() {
        return getAddress().getShippingStreetNumber();
    }

    public String getProvince() {
        return getAddress().getShippingProvince();
    }

















}

