package com.example.fromfridgetoplate.logic.model;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.NotificationBeanList;
import com.example.fromfridgetoplate.logic.exceptions.NotificationHandlingException;
import com.example.fromfridgetoplate.patterns.observer.NotificationList;

import java.util.ArrayList;
import java.util.List;

public class CachingNotificationList extends NotificationList {

    private List<Notification> notifications; // subjectstate



    public CachingNotificationList() {
        this.notifications = new ArrayList<>();
    }



    public void addNotification(Notification newNotification) { // questo sarebbbe setstate
        // Aggiunge solo le notifiche nuove e non duplicate

        if (!notifications.contains(newNotification)) {
            notifications.add(newNotification);
            notifyObs();

        }

    }



    public void clearNotifications() { // setstate
        notifications.clear();
        notifyObs();
    }

    public void removeNotification(Notification notificationToRemove) throws NotificationHandlingException { // questo sarebbbe setstate
        // Rimuove la notifica specificata dalla lista, se presente
        if (notificationToRemove != null && notifications.contains(notificationToRemove)) {
            notifications.remove(notificationToRemove);
            notifyObs();


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
                notification.markAsRead(); // Marco la notifica come letta
                toRemove = notification; // Segno per la rimozione se necessario
                found = true;
                break;
            }
        }

        // Se la notifica è stata trovata e deve essere rimossa
        if (found) {
            notifications.remove(toRemove);
            notifyObs(); // Notifica lobs
        } else {
            throw new NotificationHandlingException("La notifica da marcare come letta non è presente nella lista.");
        }
    }



    public List<Notification> getNotifications() {
        return notifications;
    }



    /*@Override
    public void notifyObs() {

        for (NotificationObserver obs : ntfObservers) {
            if (obs instanceof NotificationBeanList) {
                obs.update();
            }
        }
    }*/




}
