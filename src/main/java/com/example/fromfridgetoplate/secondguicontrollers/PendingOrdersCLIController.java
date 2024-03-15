package com.example.fromfridgetoplate.secondguicontrollers;

import com.example.fromfridgetoplate.guicontrollers.IRiderSelectionListener;
import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.OrderListBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.control.PendingOrdersController;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PendingOrdersCLIController {
    private Scanner scanner;
    private Timer refreshTimer;

    private Thread inputThread;
    private BlockingQueue<String> inputQueue = new ArrayBlockingQueue<>(1);

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
                Utils.print("ID ordine non trovato.");
            }
        } else if ("r".equals(input)) {
            exit = true;
        } else {
            Utils.print("Comando non riconosciuto.");
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
        Utils.print("\nAggiornamento degli ordini pendenti, l'aggiornamento avverà ogni 20 secondi, attendere...");

        PendingOrdersController pendingOrdersControl = new PendingOrdersController();
        OrderListBean orderListBean = pendingOrdersControl.getPendingOrderListBean();
        orders = orderListBean.getOrderBeans();

        if (orders.isEmpty()) {
            Utils.print("Non ci sono ordini in sospeso al momento.");
        } else {
            Utils.print("Ordini in sospeso:");
            for (OrderBean order : orders) {
                Utils.print("ID Ordine: " + order.getOrderId() + ", Cliente ID: " + order.getCustomerId()
                        + ", Data Ordine: " + order.getOrderTime() + ", Città Spedizione: " + order.getShippingCity());
            }
            Utils.print("Premi 'd' seguito da ID ordine per i dettagli, 'r' per tornare indietro.");
        }
    }




    private void searchRidersCLI(OrderBean selectedOrder) {
        if (selectedOrder == null) {
            Utils.print("Ordine non specificato. Riprova.");
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
        IRiderSelectionListener riderSelectionListenerCLI = new RiderSelectionListenerCLI();
        SearchBean searchBean = new SearchBean(shippingCity, riderSelectionListenerCLI, selectedOrder);
        PendingOrdersController pendingOrdersControl = new PendingOrdersController();
        return pendingOrdersControl.getAvalaibleRiders(searchBean);
    }

    private void displayAvailableRiders(List<RiderBean> availableRiders, String shippingCity) {
        Utils.print("Rider disponibili in " + shippingCity + ":");
        for (RiderBean rider : availableRiders) {
            Utils.print("ID: " + rider.getId() + ", Nome: " + rider.getName() + ", Cognome: " + rider.getSurname());
        }
        Utils.print("Inserisci l'ID del rider per assegnare l'ordine, o premi 'r' per tornare indietro.");
    }

    private void processRiderSelection(String input, List<RiderBean> availableRiders, OrderBean selectedOrder) {
        try {
            int riderId = Integer.parseInt(input);
            RiderBean selectedRider = findRiderById(availableRiders, riderId);
            if (selectedRider != null) {
                new RiderSelectionListenerCLI().onRiderSelected(selectedRider, selectedOrder);
            } else {
                Utils.print("ID rider non trovato. Riprova.");
            }
        } catch (NumberFormatException e) {
            Utils.print("Input non valido. Riprova.");
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

