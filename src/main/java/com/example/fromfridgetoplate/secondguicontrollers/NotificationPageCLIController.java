package com.example.fromfridgetoplate.secondguicontrollers;

import java.util.List;

import com.example.fromfridgetoplate.logic.bean.AddressBean;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.OrderBean;

public class NotificationPageCLIController {

    
    public void update(List<NotificationBean> notificationBeans) {
        Printer.print("----- Visualizzazione Notifiche -----");
        for (NotificationBean bean : notificationBeans) {
            OrderBean orderBean = bean.getOrderBean();
            AddressBean addressBean = orderBean.getShippingAddress();

            Printer.print("Ordine ID: " + orderBean.getOrderId());
            Printer.print("Via: " + addressBean.getShippingStreet() + ", N°: " + addressBean.getShippingStreetNumber());
            Printer.print("Città: " + addressBean.getShippingCity() + ", Provincia: " + addressBean.getShippingProvince());
            Printer.print("Messaggio: " + bean.getMessageText());
            Printer.print("------------------------------------");
        }
    }




}
