package com.example.fromfridgetoplate.secondguicontrollers;

import java.util.List;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;

public class NotificationPageCLIController {

    
    public void update(List<NotificationBean> notificationBeans) {
        System.out.println("----- Visualizzazione Notifiche -----");
        for (NotificationBean bean : notificationBeans) {
            System.out.println("Ordine ID: " + bean.getOrderId());
            System.out.println("Via: " + bean.getStreet() + ", N°: " + bean.getStreetNumber());
            System.out.println("Città: " + bean.getCity() + ", Provincia: " + bean.getProvince());
            System.out.println("Messaggio: " + bean.getMessageText());
            System.out.println("------------------------------------");
        }
    }

    public void setCallback(RiderHomeCLI riderCLIController) {
        this.riderCLIController = riderCLIController;
    }


}
