package com.example.fromfridgetoplate.secondguicontrollers;

import java.util.List;

import com.example.fromfridgetoplate.Utils;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;

public class NotificationPageCLIController {

    
    public void update(List<NotificationBean> notificationBeans) {
        Utils.print("----- Visualizzazione Notifiche -----");
        for (NotificationBean bean : notificationBeans) {
            Utils.print("Ordine ID: " + bean.getOrderId());
            Utils.print("Via: " + bean.getStreet() + ", N°: " + bean.getStreetNumber());
            Utils.print("Città: " + bean.getCity() + ", Provincia: " + bean.getProvince());
            Utils.print("Messaggio: " + bean.getMessageText());
            Utils.print("------------------------------------");
        }
    }




}
