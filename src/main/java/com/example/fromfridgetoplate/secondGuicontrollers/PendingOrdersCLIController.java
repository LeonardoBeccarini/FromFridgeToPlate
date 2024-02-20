package com.example.fromfridgetoplate.secondGuicontrollers;

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
                //loadAndDisplayOrders();
                handleOrderInput();
            } else {
                //handleRiderSearchInput();
                stopRefreshTimer();
                searchRidersCLI(selectedOrder);
                searchingRiders = false;
                setupRefreshTimer();
            }
        }
    }

    private void handleRiderSearchInput() {
    }

    private void stopInputThread() {
        if (inputThread != null) {
            inputThread.interrupt(); // Interrompo il thread di input
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
        //System.out.println("Premi 'd' seguito da ID ordine per cercare rider, 'r' per aggiornare la lista.");
        String input = scanner.nextLine().trim();
        if (input.startsWith("d")) {
            int orderId = Integer.parseInt(input.substring(1));
            selectedOrder = findOrderById(orderId);
            if (selectedOrder != null) {
                //stopRefreshTimer();
                searchingRiders = true;
                //searchRidersCLI(selectedOrder);
            } else {
                System.out.println("ID ordine non trovato.");
            }
        } else if ("r".equals(input)) {
            exit = true;
        } else {
            System.out.println("Comando non riconosciuto.");
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
        System.out.println("\nAggiornamento degli ordini pendenti, l'aggiornamento avverà ogni 20 secondi, attendere...");

        PendingOrdersController pendingOrdersControl = new PendingOrdersController();
        OrderListBean orderListBean = pendingOrdersControl.getPendingOrderListBean();
        orders = orderListBean.getOrderBeans();

        if (orders.isEmpty()) {
            System.out.println("Non ci sono ordini in sospeso al momento.");
        } else {
            System.out.println("Ordini in sospeso:");
            for (OrderBean order : orders) {
                System.out.println("ID Ordine: " + order.getOrderId() + ", Cliente ID: " + order.getCustomerId()
                        + ", Data Ordine: " + order.getOrderTime() + ", Città Spedizione: " + order.getShippingCity());
            }
            System.out.println("Premi 'd' seguito da ID ordine per i dettagli, 'r' per tornare indietro.");
        }
    }


    private void searchRidersCLI(OrderBean selectedOrder) {
        //stopInputThread(); // Interrompe il thread di input prima di iniziare la ricerca dei rider
        if (selectedOrder != null) {
            String shippingCity = selectedOrder.getShippingCity();

            // Crea un RiderSelectionListener per gestire la selezione del rider
            IRiderSelectionListener riderSelectionListenerCLI = new RiderSelectionListenerCLI();

            // Crea una SearchBean con i dettagli necessari
            SearchBean searchBean = new SearchBean(shippingCity, riderSelectionListenerCLI, selectedOrder);

            // Utilizza il controller applicativo per ottenere la lista dei rider disponibili
            PendingOrdersController pendingOrdersControl = new PendingOrdersController();
            List<RiderBean> availableRiders = pendingOrdersControl.getAvalaibleRiders(searchBean);

            // Mostra i rider disponibili
            System.out.println("Rider disponibili in " + shippingCity + ":");
            for (RiderBean rider : availableRiders) {
                System.out.println("ID: " + rider.getId() + ", Nome: " + rider.getName() + ", Cognome: " + rider.getSurname());
            }


            System.out.println("Inserisci l'ID del rider per assegnare l'ordine, o premi 'r' per tornare indietro.");
            String input = scanner.nextLine().trim();

            if ("r".equals(input)) {
                return; // Torna al menu precedente
            } else {
                try {
                    int riderId = Integer.parseInt(input);
                    RiderBean selectedRider = null;
                    for (RiderBean rider : availableRiders) {
                        if (rider.getId() == riderId) {
                            selectedRider = rider;
                            System.out.println("rider id:" + riderId);
                            break;
                        }
                    }

                    if (selectedRider != null) {
                        // Simula la selezione del rider come farebbe il RiderSelectionListener in un contesto GUI
                        riderSelectionListenerCLI.onRiderSelected(selectedRider, selectedOrder);
                    } else {
                        System.out.println("ID rider non trovato. Riprova.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input non valido. Riprova.");
                }
            }
        } else {
            System.out.println("Ordine non specificato. Riprova.");
        }


    }


    /*public static void main (String[]args){
        PendingOrdersCLIController controller = new PendingOrdersCLIController();
        controller.displayPendingOrders();
    }*/


}

