package com.example.fromfridgetoplate.secondguicontrollers;

import java.util.List;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;

public class NotificationPageCLIController {

    
    public void update(List<NotificationBean> notificationBeans) {
        Printer.print("----- Visualizzazione Notifiche -----");
        for (NotificationBean bean : notificationBeans) {
            Printer.print("Ordine ID: " + bean.getOrderId());
            Printer.print("Via: " + bean.getStreet() + ", N°: " + bean.getStreetNumber());
            Printer.print("Città: " + bean.getCity() + ", Provincia: " + bean.getProvince());
            Printer.print("Messaggio: " + bean.getMessageText());
            Printer.print("------------------------------------");
        }
    }




}
