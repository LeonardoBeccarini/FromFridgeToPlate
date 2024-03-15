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
        nlb.setGraphicController(this);
    }

    public void mainMenu() {
        boolean isRunning = true;
        while (isRunning) {
            Printer.print("\n*** Menu Principale Rider ***");
            Printer.print("1. Vai Online");
            Printer.print("2. Vai Offline");
            Printer.print("3. Sezione Notifiche");
            Printer.print("4. Accedi al tuo report delle consegne");
            Printer.print("5. Visualizza l'ordine che devi consegnare");
            Printer.print("6. Esci");
            Printer.print("Seleziona un'opzione: ");
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
                    Printer.print("Uscita dal sistema...");
                    isRunning = false;
                    break;
                default:
                    Printer.print("Opzione non valida, riprova.");
            }
        }
    }

    private void goOnline() {
        if (!isOnline) {
            isOnline = true;
            this.riderController = new RiderHPController(nlb);

            Printer.print("Sei entrato in servizio");


            riderController.setRiderAvailable(true);
            riderController.startNotificationPolling();
            Printer.print("Sei ora online e disponibile per le consegne.");
        } else {
            Printer.print("Sei già online.");
        }
    }

    private void goOffline() {
        if (isOnline) {
            isOnline = false;
            riderController.stopNotificationPolling(); // Simula la disattivazione del polling delle notifiche
            Printer.print("Sei ora offline e non disponibile per le consegne.");
        } else {
            Printer.print("Sei già offline.");
        }
    }

    private void viewNotifications() {
        if (!isOnline) {
            Printer.print("Devi essere online per visualizzare le notifiche.");
            return;
        }

        List<NotificationBean> notificationBeans = nlb.getNotifications();
        if (notificationBeans.isEmpty()) {
            Printer.print("Non ci sono nuove notifiche.");
            return;
        }

        Printer.print("----- Visualizzazione Notifiche -----");
        for (NotificationBean bean : notificationBeans) {
            Printer.print("Ordine ID: " + bean.getOrderId());
            Printer.print("Via: " + bean.getStreet() + ", N°: " + bean.getStreetNumber());
            Printer.print("Città: " + bean.getCity() + ", Provincia: " + bean.getProvince());
            Printer.print("Messaggio: " + bean.getMessageText());
            Printer.print("------------------------------------");
        }

        handleNotification(notificationBeans);
    }

    private void handleNotification(List<NotificationBean> notifications) {
        Printer.print("\nSeleziona la notifica da gestire o 0 per tornare indietro:");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= notifications.size()) {
            NotificationBean selectedNotification = notifications.get(choice - 1);
            Printer.print("Hai selezionato la notifica per l'ordine ID: " + selectedNotification.getOrderId());

            Printer.print("1. Accetta Notifica");
            Printer.print("2. Rifiuta Notifica");
            Printer.print("Seleziona un'azione: ");
            int action = scanner.nextInt();

            switch (action) {
                case 1:
                    acceptNotification(selectedNotification);
                    break;
                case 2:
                    declineNotification(selectedNotification);
                    break;
                default:
                    Printer.print("Azione non valida.");
            }
        } else if (choice != 0) {
            Printer.print("Selezione non valida.");
        }
    }

    private void acceptNotification(NotificationBean notification) {
        RiderBean currentRider = riderController.getRiderDetailsFromSession();

        // Verifica se il rider ha già un ordine in consegna
        boolean hasOrderInDelivery = riderController.checkForOrderInDelivery(currentRider);

        if (hasOrderInDelivery) {
            Printer.print("Consegna in corso: Hai già un ordine in consegna. Devi completare la consegna corrente prima di accettarne un'altra.");
        } else {
            try {
                riderController.acceptOrder(notification);
                riderController.markNotificationAsRead(notification); // Qui si suppone che il metodo markNotificationAsRead simuli l'accettazione dell'ordine
                Printer.print("Hai accettato l'ordine con ID: " + notification.getOrderId());
            } catch (Exception e) {
                Printer.print("Errore nell'accettazione dell'ordine: " + e.getMessage());
            }
        }
    }


    private void declineNotification(NotificationBean notification) {
        try {
            riderController.declineOrder(notification);
            riderController.markNotificationAsRead(notification);
            Printer.print("Hai rifiutato la notifica per l'ordine ID: " + notification.getOrderId());
        } catch (Exception e) {
            Printer.print("Errore nel rifiuto della notifica: " + e.getMessage());
        }
    }

    @Override
    public void update(List<NotificationBean> notifications) {
        Printer.print("I tuoi incarichi sono stati aggiornati. Potresti avere nuove notifiche. Seleziona 'Visualizza Notifiche' dal menu principale per vederle.");
    }

    public static void main(String[] args) {
        new RiderHomePageCLIController().mainMenu();
    }
}
