package com.example.fromfridgetoplate.secondguicontrollers;


import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.bean.NotificationListBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.control.RiderHPController;
import java.util.List;
import java.util.Scanner;

public class RiderHomePageCLIController implements NotificationObserver {

    private RiderHPController riderController;
    private NotificationListBean nlb = new NotificationListBean();
    private Scanner scanner = new Scanner(System.in);
    private boolean isOnline = false;

    public RiderHomePageCLIController() {
        //nlb.setGraphicController(this);
        nlb.attach(this);
    }

    public void mainMenu() {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n*** Menu Principale Rider ***");
            System.out.println("1. Vai Online");
            System.out.println("2. Vai Offline");
            System.out.println("3. Sezione Notifiche");
            System.out.println("4. Accedi al tuo report delle consegne");
            System.out.println("5. Visualizza l'ordine che devi consegnare");
            System.out.println("6. Esci");
            System.out.print("Seleziona un'opzione: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    goOnline();
                    break;

                case 2:
                    goOffline();
                    break;

                case 3:
                    viewNotifications();
                    break;

                case 4:
                    RiderDeliveryReportCLIController rdrCtrl = new RiderDeliveryReportCLIController();
                    rdrCtrl.showDeliveryReport();
                    break;

                case 5:
                    RiderCurrentOrderCLIController rcoCtrl = new RiderCurrentOrderCLIController();
                    rcoCtrl.loadOrderDetails();
                    break;

                case 6:
                    System.out.println("Uscita dal sistema...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Opzione non valida, riprova.");
            }
        }
    }

    private void goOnline() {
        if (!isOnline) {
            isOnline = true;
            this.riderController = new RiderHPController(nlb);

            System.out.println("Sei entrato in servizio");


            riderController.setRiderAvailable(true);
            riderController.startNotificationPolling();
            System.out.println("Sei ora online e disponibile per le consegne.");
        } else {
            System.out.println("Sei già online.");
        }
    }

    private void goOffline() {
        if (isOnline) {
            isOnline = false;
            riderController.stopNotificationPolling(); // Simula la disattivazione del polling delle notifiche
            System.out.println("Sei ora offline e non disponibile per le consegne.");
        } else {
            System.out.println("Sei già offline.");
        }
    }

    private void viewNotifications() {
        if (!isOnline) {
            System.out.println("Devi essere online per visualizzare le notifiche.");
            return;
        }

        List<NotificationBean> notificationBeans = nlb.getNotifications();
        if (notificationBeans.isEmpty()) {
            System.out.println("Non ci sono nuove notifiche.");
            return;
        }

        System.out.println("----- Visualizzazione Notifiche -----");
        for (NotificationBean bean : notificationBeans) {
            System.out.println("Ordine ID: " + bean.getOrderId());
            System.out.println("Via: " + bean.getStreet() + ", N°: " + bean.getStreetNumber());
            System.out.println("Città: " + bean.getCity() + ", Provincia: " + bean.getProvince());
            System.out.println("Messaggio: " + bean.getMessageText());
            System.out.println("------------------------------------");
        }

        handleNotification(notificationBeans);
    }

    private void handleNotification(List<NotificationBean> notifications) {
        System.out.println("\nSeleziona la notifica da gestire o 0 per tornare indietro:");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= notifications.size()) {
            NotificationBean selectedNotification = notifications.get(choice - 1);
            System.out.printf("Hai selezionato la notifica per l'ordine ID: %d%n", selectedNotification.getOrderId());

            System.out.println("1. Accetta Notifica");
            System.out.println("2. Rifiuta Notifica");
            System.out.print("Seleziona un'azione: ");
            int action = scanner.nextInt();

            switch (action) {
                case 1:
                    acceptNotification(selectedNotification);
                    break;
                case 2:
                    declineNotification(selectedNotification);
                    break;
                default:
                    System.out.println("Azione non valida.");
            }
        } else if (choice != 0) {
            System.out.println("Selezione non valida.");
        }
    }

    private void acceptNotification(NotificationBean notification) {
        RiderBean currentRider = riderController.getRiderDetailsFromSession();

        // Verifica se il rider ha già un ordine in consegna
        boolean hasOrderInDelivery = riderController.checkForOrderInDelivery(currentRider);

        if (hasOrderInDelivery) {
            System.out.println("Consegna in corso: Hai già un ordine in consegna. Devi completare la consegna corrente prima di accettarne un'altra.");
        } else {
            try {
                riderController.acceptOrder(notification);
                riderController.markNotificationAsRead(notification); // Qui si suppone che il metodo markNotificationAsRead simuli l'accettazione dell'ordine
                System.out.printf("Hai accettato l'ordine con ID: %d%n", notification.getOrderId());
            } catch (Exception e) {
                System.out.println("Errore nell'accettazione dell'ordine: " + e.getMessage());
            }
        }
    }


    private void declineNotification(NotificationBean notification) {
        try {
            riderController.declineOrder(notification);
            riderController.markNotificationAsRead(notification);
            System.out.printf("Hai rifiutato la notifica per l'ordine ID: %d%n", notification.getOrderId());
        } catch (Exception e) {
            System.out.println("Errore nel rifiuto della notifica: " + e.getMessage());
        }
    }

    @Override
    public void update() {
        System.out.println("I tuoi incarichi sono stati aggiornati. Potresti avere nuove notifiche. Seleziona 'Visualizza Notifiche' dal menu principale per vederle.");
    }

    public static void main(String[] args) {
        new RiderHomePageCLIController().mainMenu();
    }
}
