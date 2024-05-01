package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.guicontrollers.RiderHomePageGraphicController;
import com.example.fromfridgetoplate.logic.model.Notification;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.secondguicontrollers.IUpdateable;

import java.util.ArrayList;
import java.util.List;

public class NotificationBeanList implements NotificationObserver {

    private List<NotificationBean> notificationBeans = new ArrayList<>();
    private IUpdateable rgc;
    //private CachingNotificationList notificationList;



    //public NotificationBeanList(CachingNotificationList notificationList) {
        // Collega questa lista di bean di notifica alla lista di notifica principale
        //this.notificationList = notificationList;
        //this.notificationList.attach(this); // si registra come observer
   // }

    public NotificationBeanList(IUpdateable rgc) {
        this.rgc = rgc;
    }


    @Override
    public void update() {

    }

    @Override
    public void update(List<NotificationBean> ntfBeans) {
        // Sincronizza i NotificationBean con le Notification nella lista principale
        synchronizeBeans(ntfBeans);
        rgc.update(notificationBeans);
    }

    private void synchronizeBeans(List<NotificationBean> ntfBeans) {
        // Svuota la lista esistente per riempirla con le notifiche aggiornate
        notificationBeans.clear();
        setNotificationBeans(ntfBeans);


    }

    private OrderBean convertToOrderBean(Order order) {
        AddressBean address = new AddressBean(
                order.getShippingStreet(),
                order.getShippingStreetNumber(),
                order.getShippingCity(),
                order.getShippingProvince()
        );

        return new OrderBean(
                order.getOrderId(),
                address
        );
    }

    public List<NotificationBean> getNotificationBeans() {
        return new ArrayList<>(notificationBeans); // Restituisco una copia per evitare modifiche esterne
    }



    private void setNotificationBeans(List<NotificationBean> notificationBeans) {
        this.notificationBeans = notificationBeans;
    }



}
