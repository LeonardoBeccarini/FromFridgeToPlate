package com.example.fromfridgetoplate.secondguicontrollers;


import com.example.fromfridgetoplate.Utils;
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
        nlb.setGraphicController(this);
    }

    public void mainMenu() {
        boolean isRunning = true;
        while (isRunning) {
            Utils.print("\n*** Menu Principale Rider ***");
            Utils.print("1. Vai Online");
            Utils.print("2. Vai Offline");
            Utils.print("3. Sezione Notifiche");
            Utils.print("4. Accedi al tuo report delle consegne");
            Utils.print("5. Visualizza l'ordine che devi consegnare");
            Utils.print("6. Esci");
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
                    Utils.print("Uscita dal sistema...");
                    isRunning = false;
                    break;
                default:
                    Utils.print("Opzione non valida, riprova.");
            }
        }
    }

    private void goOnline() {
        if (!isOnline) {
            isOnline = true;
            this.riderController = new RiderHPController(nlb);

            Utils.print("Sei entrato in servizio");


            riderController.setRiderAvailable(true);
            riderController.startNotificationPolling();
            Utils.print("Sei ora online e disponibile per le consegne.");
        } else {
            Utils.print("Sei già online.");
        }
    }

    private void goOffline() {
        if (isOnline) {
            isOnline = false;
            riderController.stopNotificationPolling(); // Simula la disattivazione del polling delle notifiche
            Utils.print("Sei ora offline e non disponibile per le consegne.");
        } else {
            Utils.print("Sei già offline.");
        }
    }

    private void viewNotifications() {
        if (!isOnline) {
            Utils.print("Devi essere online per visualizzare le notifiche.");
            return;
        }

        List<NotificationBean> notificationBeans = nlb.getNotifications();
        if (notificationBeans.isEmpty()) {
            Utils.print("Non ci sono nuove notifiche.");
            return;
        }

        Utils.print("----- Visualizzazione Notifiche -----");
        for (NotificationBean bean : notificationBeans) {
            Utils.print("Ordine ID: " + bean.getOrderId());
            Utils.print("Via: " + bean.getStreet() + ", N°: " + bean.getStreetNumber());
            Utils.print("Città: " + bean.getCity() + ", Provincia: " + bean.getProvince());
            Utils.print("Messaggio: " + bean.getMessageText());
            Utils.print("------------------------------------");
        }

        handleNotification(notificationBeans);
    }

    private void handleNotification(List<NotificationBean> notifications) {
        Utils.print("\nSeleziona la notifica da gestire o 0 per tornare indietro:");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= notifications.size()) {
            NotificationBean selectedNotification = notifications.get(choice - 1);
            System.out.printf("Hai selezionato la notifica per l'ordine ID: %d%n", selectedNotification.getOrderId());

            Utils.print("1. Accetta Notifica");
            Utils.print("2. Rifiuta Notifica");
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
                    Utils.print("Azione non valida.");
            }
        } else if (choice != 0) {
            Utils.print("Selezione non valida.");
        }
    }

    private void acceptNotification(NotificationBean notification) {
        RiderBean currentRider = riderController.getRiderDetailsFromSession();

        // Verifica se il rider ha già un ordine in consegna
        boolean hasOrderInDelivery = riderController.checkForOrderInDelivery(currentRider);

        if (hasOrderInDelivery) {
            Utils.print("Consegna in corso: Hai già un ordine in consegna. Devi completare la consegna corrente prima di accettarne un'altra.");
        } else {
            try {
                riderController.acceptOrder(notification);
                riderController.markNotificationAsRead(notification); // Qui si suppone che il metodo markNotificationAsRead simuli l'accettazione dell'ordine
                System.out.printf("Hai accettato l'ordine con ID: %d%n", notification.getOrderId());
            } catch (Exception e) {
                Utils.print("Errore nell'accettazione dell'ordine: " + e.getMessage());
            }
        }
    }


    private void declineNotification(NotificationBean notification) {
        try {
            riderController.declineOrder(notification);
            riderController.markNotificationAsRead(notification);
            System.out.printf("Hai rifiutato la notifica per l'ordine ID: %d%n", notification.getOrderId());
        } catch (Exception e) {
            Utils.print("Errore nel rifiuto della notifica: " + e.getMessage());
        }
    }

    @Override
    public void update(List<NotificationBean> notifications) {
        Utils.print("I tuoi incarichi sono stati aggiornati. Potresti avere nuove notifiche. Seleziona 'Visualizza Notifiche' dal menu principale per vederle.");
    }

    public static void main(String[] args) {
        new RiderHomePageCLIController().mainMenu();
    }
}
