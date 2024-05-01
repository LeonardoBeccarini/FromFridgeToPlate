package com.example.fromfridgetoplate.logic.model;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.logic.bean.AddressBean;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.NotificationBeanList;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.exceptions.NotificationHandlingException;
import com.example.fromfridgetoplate.patterns.observer.NotificationList;

import java.util.ArrayList;
import java.util.List;

public class CachingNotificationList extends NotificationList {

    protected List<Notification> notifications; // subjectstate



    public CachingNotificationList() {
        this.notifications = new ArrayList<>();
    }



    public void addNotifications(List<Notification> newNotifications) { // questo sarebbbe setstate
        // Aggiunge solo le notifiche nuove e non duplicate
        for (Notification newNotification : newNotifications) {
            if (!notifications.contains(newNotification)) {
                notifications.add(newNotification);
            }
        }

        notifyGUI();
    }

    public void addNotification(Notification newNotification) { // questo sarebbbe setstate
        // Aggiunge solo le notifiche nuove e non duplicate

        if (!notifications.contains(newNotification)) {
            notifications.add(newNotification);
            notifyGUI();

        }

    }



    public void clearNotifications() { // setstate
        notifications.clear();
        notifyGUI();
    }

    public void removeNotification(Notification notificationToRemove) throws NotificationHandlingException { // questo sarebbbe setstate
        // Rimuove la notifica specificata dalla lista, se presente
        if (notificationToRemove != null && notifications.contains(notificationToRemove)) {
            notifications.remove(notificationToRemove);
            notifyGUI(); //  Notifica il controller grafico dell'aggiornamento


        } else {

            throw new NotificationHandlingException("La notifica da rimuovere non è presente nella lista.");

        }

    }

    public void updateNotificationAsRead(NotificationBean readNotification) throws NotificationHandlingException {
        if (readNotification == null) {
            throw new IllegalArgumentException("La notifica fornita è null.");
        }

        boolean found = false;
        Notification toRemove = null;

        // Cerca la notifica corrispondente nell'elenco basandosi sull'ID della notifica
        for (Notification notification : notifications) {
            if (notification.getNotificationId() == readNotification.getNotificationId()) {
                notification.markAsRead(); // Marca la notifica come letta
                toRemove = notification; // Segna per la rimozione se necessario
                found = true;
                break;
            }
        }

        // Se la notifica è stata trovata e deve essere rimossa
        if (found && toRemove != null) {
            notifications.remove(toRemove);
            notifyGUI(); // Notifica il controller grafico dell'aggiornamento
        } else {
            throw new NotificationHandlingException("La notifica da marcare come letta non è presente nella lista.");
        }
    }




    public List<Notification> getNotifications() {
        return notifications;
    }



    @Override
    public void notifyGUI() {
        super.notifyGUI();  // Notifica tutti gli observer senza parametri
        List<NotificationBean> ntfBeans = convertToNotificationBeans(this.notifications);

        for (NotificationObserver obs : ntfObservers) {
            if (obs instanceof NotificationBeanList) {
                obs.update(ntfBeans);
            }
        }
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





















}
