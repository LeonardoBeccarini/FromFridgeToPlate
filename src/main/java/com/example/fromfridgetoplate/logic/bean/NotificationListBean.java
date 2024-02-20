package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.guicontrollers.RiderHomePageGraphicController;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.exceptions.NotificationHandlingException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.fromfridgetoplate.guicontrollers.RiderNotificationPageGraphicController;
import java.util.ArrayList;
import java.util.List;


public class NotificationListBean {
    private List<NotificationBean> notifications;

    private NotificationObserver graphicController; // controllerJavaFx o controllerCLI

    public NotificationListBean() {
        this.notifications = new ArrayList<>();
    }

    public void setGraphicController(NotificationObserver controller) {
        this.graphicController = controller;
    }

    public void addNotifications(List<NotificationBean> newNotifications) {
        // Aggiunge solo le notifiche nuove e non duplicate
        for (NotificationBean newNotification : newNotifications) {
            if (!notifications.contains(newNotification)) {
                notifications.add(newNotification);
            }
        }

        notifyGraphicController();
    }

    public void clearNotifications() {
        notifications.clear();
        notifyGraphicController();
    }

    public void removeNotification(NotificationBean notificationToRemove) throws NotificationHandlingException {
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

        if (graphicController != null) {
            //System.out.println("check");
            graphicController.update(notifications);

        }
        else {
            //System.out.println("graphicController è null");
        }

    }


}
