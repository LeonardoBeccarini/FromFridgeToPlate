package com.example.fromfridgetoplate.secondGuicontrollers;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.guicontrollers.RiderNotificationPageGraphicController;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.NotificationListBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.Scanner;
import java.util.List;


public class RiderHomeCLI implements NotificationObserver {
    private Scanner scanner;
    private RiderHPController riderController;
    private NotificationListBean nlb = new NotificationListBean();
    //private NotificationPageCLIController notificationPageCLIController;
    private boolean isOnline = false;


    public RiderHomeCLI() {
        this.scanner = new Scanner(System.in);
        //this.riderController = new RiderHPController();

    }


    public void displayMenu() {
        while (true) {
            System.out.println("----- Rider Home Page -----");
            System.out.println("1. Go Online");
            System.out.println("2. Go Offline");
            System.out.println("3. Check Notifications");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    goOnline();
                    break;
                case 2:
                    goOffline();
                    break;
                case 3:
                    //checkNotifications();
                    break;
                case 4:
                    System.out.println("Exiting Rider Home Page.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void goOnline() {

        if (!isOnline) {
            isOnline = true;

            // Recupero i dettagli del rider dalla sessione, come nel controller grafico
            RiderBean riderBn = riderController.getRiderDetailsFromSession();

            if (riderBn == null) {
                System.out.println("Errore di Login: Dettagli Rider Non Trovati. Assicurati di essere loggato correttamente.");
                return;
            }



            this.riderController = new RiderHPController(riderBn, nlb); // Assumendo che NotificationListBeanCLI sia l'adattamento per CLI
            nlb.setGraphicController(this);
            System.out.println("Rider ID: " + riderBn.getId() + " Nome: " + riderBn.getName() + " Cognome: " + riderBn.getSurname());
            System.out.println("Entra in servizio");


            // Inizio del polling delle notifiche
            riderController.startNotificationPolling();
        } else {
            System.out.println("Sei già online.");
        }
    }


    private void goOffline() {
        if (isOnline) {
            isOnline = false;

            System.out.println("You are now offline.");
        }
    }

    public void update(List<NotificationBean> notificationBeans) {

        if (!notificationBeans.isEmpty()) {
            System.out.println("Hai " + notificationBeans.size() + " nuove notifiche:");
            NotificationPageCLIController notificationPageCLIController = new NotificationPageCLIController();
            notificationPageCLIController.update(notificationBeans);
        } else {
            System.out.println("Non ci sono nuove notifiche al momento.");
        }
    }

}
