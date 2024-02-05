package com.example.fromfridgetoplate.secondGuicontrollers;

import java.util.List;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;

public class NotificationPageCLIController {

    private RiderHomeCLI riderCLIController;

    /*public NotificationPageCLIController(RiderHomeCLI riderCLIController) {
        this.riderCLIController = riderCLIController;
    }*/

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
