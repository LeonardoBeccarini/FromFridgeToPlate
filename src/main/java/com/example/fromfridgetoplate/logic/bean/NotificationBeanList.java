package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.secondguicontrollers.IUpdateable;

import java.util.ArrayList;
import java.util.List;

public class NotificationBeanList implements NotificationObserver {

    private List<NotificationBean> notificationBeans = new ArrayList<>();
    private IUpdateable rgc; // graphiccontroller


    public NotificationBeanList(IUpdateable rgc) {
        this.rgc = rgc;
    }


    @Override
    public void update() {
        //to notify observer
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

    private void setNotificationBeans(List<NotificationBean> notificationBeans) {
        this.notificationBeans = notificationBeans;
    }



}
