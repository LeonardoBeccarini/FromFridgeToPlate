package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.logic.model.CachingNotificationList;
import com.example.fromfridgetoplate.logic.model.Notification;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.secondguicontrollers.IUpdateable;

import java.util.ArrayList;
import java.util.List;

public class NotificationBeanList implements NotificationObserver {

    private List<NotificationBean> notificationBeans = new ArrayList<>();
    CachingNotificationList notificationList; // sarebbe il concreteSubject del pattern
    private IUpdateable rgc; // graphiccontroller


    public NotificationBeanList(IUpdateable rgc) {
        this.rgc = rgc;
    }


    @Override
    public void update() {
        //to notify observer
        synchronizeBeans(convertToNotificationBeans(notificationList.getNotifications()));
        rgc.update(notificationBeans);
    }

    /*@Override
    public void update(List<NotificationBean> ntfBeans) {
        // Sincronizza i NotificationBean con le Notification nella lista principale
        synchronizeBeans(ntfBeans);
        rgc.update(notificationBeans);
    }*/

    private void synchronizeBeans(List<NotificationBean> ntfBeans) {
        // Svuota la lista esistente per riempirla con le notifiche aggiornate
        notificationBeans.clear();
        setNotificationBeans(ntfBeans);


    }

    private List <NotificationBean> convertToNotificationBeans(List <Notification> notifications) {

        List <NotificationBean> ntfBeanLst = new ArrayList<>();

        for (Notification notification : notifications) {

            Order order = notification.getOrder();
            AddressBean addressBean = new AddressBean(order.getShippingStreet(), order.getShippingStreetNumber(), order.getShippingCity(), order.getShippingProvince());
            OrderBean orderBean = new OrderBean(order.getRiderId(), order.getOrderId(), addressBean);

            NotificationBean ntfBean = new NotificationBean(
                    orderBean,
                    notification.getMessageText()
            );
            ntfBean.setNotificationId(notification.getNotificationId());
            ntfBeanLst.add(ntfBean);

        }

        return ntfBeanLst;
    }

    private void setNotificationBeans(List<NotificationBean> notificationBeans) {
        this.notificationBeans = notificationBeans;
    }


    public void setConcreteSubj(CachingNotificationList notificationList) {
        this.notificationList = notificationList;
    }
}
