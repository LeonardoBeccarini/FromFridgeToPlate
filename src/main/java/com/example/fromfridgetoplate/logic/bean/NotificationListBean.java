package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.guicontrollers.RiderHomePageGraphicController;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.exceptions.NotificationHandlingException;
import com.example.fromfridgetoplate.patterns.observer.NotificationList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.fromfridgetoplate.guicontrollers.RiderNotificationPageGraphicController;
import java.util.ArrayList;
import java.util.List;


public class NotificationListBean extends NotificationList {

    // private NotificationObserver graphicController; // controllerJavaFx o controllerCLI

    public NotificationListBean() {
        this.notifications = new ArrayList<>();
    }// subjectstate



    public void addNotifications(List<NotificationBean> newNotifications) { // questo sarebbbe setstate
        // Aggiunge solo le notifiche nuove e non duplicate
        for (NotificationBean newNotification : newNotifications) {
            if (!notifications.contains(newNotification)) {
                notifications.add(newNotification);
            }
        }

        notifyGraphicController();
    }



    public void clearNotifications() { // setstate
        notifications.clear();
        notifyGraphicController();
    }

    public void removeNotification(NotificationBean notificationToRemove) throws NotificationHandlingException { // questo sarebbbe setstate
        // Rimuove la notifica specificata dalla lista, se presente
        if (notificationToRemove != null && notifications.contains(notificationToRemove)) {
            notifications.remove(notificationToRemove);
            notifyGraphicController(); //  Notifica il controller grafico dell'aggiornamento


        } else {

            throw new NotificationHandlingException("La notifica da rimuovere non è presente nella lista.");

        }

    }


    public List<NotificationBean> getNotifications() {
        return notifications;
    }


    private void notifyGraphicController() {

        //graphicController.update(notifications);
        notifyGUI();
    }


}
