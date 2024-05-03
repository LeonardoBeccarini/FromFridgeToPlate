package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.control.PendingOrdersController;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;

import java.io.IOException;
import java.util.*;

public class PendingOrdersCLIController {
    private Scanner scanner;
    private Timer refreshTimer;



    private boolean searchingRiders = false;
    private boolean exit = false;

    List<OrderBean> orders;

    OrderBean selectedOrder;

    public PendingOrdersCLIController() {
        this.scanner = new Scanner(System.in);
    }


    public void displayPendingOrders() throws IOException {
        setupRefreshTimer();
        while (true) {

            if (exit){
                stopRefreshTimer();
                NavigatorCLI.getInstance().goTo("ResellerHomeCLI");
                return;
            }
            if (!searchingRiders) {

                handleOrderInput();
            } else {

                stopRefreshTimer();
                searchRidersCLI(selectedOrder);
                searchingRiders = false;
                setupRefreshTimer();
            }
        }
    }


    private void setupRefreshTimer() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                loadAndDisplayOrders();
            }
        }, 0, 20000); // Aggiorno ogni 20 secondi
    }


    private void stopRefreshTimer() {
        if (refreshTimer != null) {
            refreshTimer.cancel(); // Cancelliamoil timer
            refreshTimer.purge(); // Rimuoviamo i task cancellati dalla coda del timer
            refreshTimer = null; // Imposto poi il timer su null per dire che non è più attivo
        }
    }


    private void handleOrderInput() {
        String input = scanner.nextLine().trim();
        if (input.startsWith("d")) {
            int orderId = Integer.parseInt(input.substring(1));
            selectedOrder = findOrderById(orderId);
            if (selectedOrder != null) {
                searchingRiders = true;
            } else {
                Printer.print("ID ordine non trovato. Premi 'r' per tornare indietro");
            }
        } else if ("r".equals(input)) {
            exit = true;
        } else {
            Printer.print("Comando non riconosciuto.");
        }
    }


    public OrderBean findOrderById(int orderId) {
        for (OrderBean order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null; // Nessun ordine trovato con quell'ID
    }


    private void loadAndDisplayOrders() {
        Printer.print("\nAggiornamento degli ordini pendenti, l'aggiornamento avverà ogni 20 secondi, attendere...");

        try {
            PendingOrdersController pendingOrdersControl = new PendingOrdersController();
            this.orders = pendingOrdersControl.getUpdatedPendingOrders();

            if (orders.isEmpty()) {
                Printer.print("Non ci sono ordini in sospeso al momento. Premi 'r' per tornare indietro.");
            } else {
                Printer.print("Ordini in sospeso:");
                for (OrderBean order : orders) {
                    Printer.print("ID Ordine: " + order.getOrderId() + ", Cliente ID: " + order.getCustomerId()
                            + ", Data Ordine: " + order.getOrderTime() + ", Città Spedizione: " + order.getShippingCity());
                }
                Printer.print("Premi 'd' seguito da ID ordine per i dettagli, 'r' per tornare indietro.");
            }
        } catch (DAOException e) {
            Printer.print("Si è verificato un errore durante il recupero degli ordini pendenti: " + e.getMessage());
            Printer.print("Premi 'r' per tornare indietro.");
        }
    }





    private void searchRidersCLI(OrderBean selectedOrder) {
        if (selectedOrder == null) {
            Printer.print("Ordine non specificato. Riprova.");
            return;
        }

        String shippingCity = selectedOrder.getShippingCity();
        List<RiderBean> availableRiders = getAvailableRiders(shippingCity, selectedOrder);
        displayAvailableRiders(availableRiders, shippingCity);

        String input = scanner.nextLine().trim();
        if ("r".equals(input)) {
            return; // Torna al menu precedente
        }

        processRiderSelection(input, availableRiders, selectedOrder);
    }

    private List<RiderBean> getAvailableRiders(String shippingCity, OrderBean selectedOrder) {
        SearchBean searchBean = new SearchBean(shippingCity, selectedOrder);
        PendingOrdersController pendingOrdersControl = new PendingOrdersController();
        try {
            return pendingOrdersControl.getAvalaibleRiders(searchBean);
        } catch (DAOException e) {

            Printer.print("Errore nel recuperare i riders disponibili: " + e.getMessage());
            return new ArrayList<>(); // Restituisco una lista vuota in caso di errore
        }
    }


    private void displayAvailableRiders(List<RiderBean> availableRiders, String shippingCity) {
        Printer.print("Rider disponibili in " + shippingCity + ":");
        for (RiderBean rider : availableRiders) {
            Printer.print("ID: " + rider.getId() + ", Nome: " + rider.getName() + ", Cognome: " + rider.getSurname());
        }
        Printer.print("Inserisci l'ID del rider per assegnare l'ordine, o premi 'r' per tornare indietro.");
    }

    private void processRiderSelection(String input, List<RiderBean> availableRiders, OrderBean selectedOrder) {
        try {
            int riderId = Integer.parseInt(input);
            RiderBean selectedRider = findRiderById(availableRiders, riderId);
            if (selectedRider != null) {
                new RiderSelectionListenerCLI().onRiderSelected(selectedRider, selectedOrder);
            } else {
                Printer.print("ID rider non trovato. Riprova.");
            }
        } catch (NumberFormatException e) {
            Printer.print("Input non valido. Riprova.");
        }
    }

    private RiderBean findRiderById(List<RiderBean> availableRiders, int riderId) {
        for (RiderBean rider : availableRiders) {
            if (rider.getId() == riderId) {
                return rider;
            }
        }
        return null;
    }





}

