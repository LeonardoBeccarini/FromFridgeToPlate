package com.example.fromfridgetoplate.logic.model;

public class Notification {

    private int notificationId;
    private Order order;
    private String messageText;

    private boolean isRead;

    public Notification(Order order, String messageText) {
        this.order = order;
        this.messageText = messageText;
    }

    public Notification(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public boolean isRead() {
        return isRead;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}

