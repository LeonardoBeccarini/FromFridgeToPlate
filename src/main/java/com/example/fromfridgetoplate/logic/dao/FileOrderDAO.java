package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.OrderNotFoundException;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileOrderDAO extends FileDAOBase implements OrderDAO {


    public FileOrderDAO() {

    }

    public void acceptOrder(int orderId, int riderId) {
        List<Order> orders = getAllAssignedOrders(); // Carica tutti gli ordini ass.ti
        boolean orderFound = false;

        for (Order order : orders) {
            System.out.println("assOrder: " + order.getOrderId()+ " status: " + order.getStatus());
            if (order.getOrderId() == orderId) {
                order.setStatus("in consegna");
                order.setAcceptedByRider(true);
                order.setRiderId(riderId);
                orderFound = true;
                break; //interrompo dopo che l'ordine è stato trovato e aggiornato
            }
        }

        if (orderFound) {
            try {
                writeAssignedOrdersToFile(orders); // Salva l'elenco degli ordini aggiornato sul file
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Ordine con ID " + orderId + " non trovato.");
        }
    }





    public void declineOrder(int orderId, int riderId) throws IOException {

        List<Order> orders = getAllOrders();
        boolean orderUpdated = false;

        // Trova l'ordine con l'ID e il riderId specificati e aggiorna lo stato
        for (Order order : orders) {
            if (order.getOrderId() == orderId && order.getRiderId() == riderId) {
                System.out.println("orderid:" + order.getOrderId() + " status: " + order.getStatus());
                order.setStatus("pronto");
                order.setRiderId(0); // Rimuovi l'associazione con il rider
                order.setAcceptedByRider(false); // Indica che l'ordine non è accettato da nessun rider
                orderUpdated = true;
                break; // Interrompo il ciclo una volta trovato e aggiornato l'ordine
            }
        }

        // Se l'ordine è stato trovato e aggiornato, riserializzo la lista aggiornata degli ordini
        if (orderUpdated) {
            writeOrdersToFile(orders);
        } else {
            System.out.println("Ordine con ID " + orderId + " e RiderId " + riderId + " non trovato.");
        }

        // Carico gli ordini assegnati e rimuovi l'ordine declinato
        List<Order> assignedOrders = getAllAssignedOrders();
        Order orderToRemove = null;

        // Cerco l'ordine da rimuovere
        for (Order order : assignedOrders) {
            if (order.getOrderId() == orderId) {
                orderToRemove = order;
                break;
            }
        }
        // Rimuovo l'ordine se trovato
        if (orderToRemove != null) {
            assignedOrders.remove(orderToRemove);
            try {
                writeAssignedOrdersToFile(assignedOrders);
            }
                catch(IOException e ){
                    System.err.println("Errore nella rimozione dell' elemento : " + e.getMessage());
            }

        }

        // Salvo la lista aggiornata degli ordini assegnati
        try {
            writeAssignedOrdersToFile(assignedOrders);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }







    public OrderList getConfirmedDeliveriesForRider(int riderId) {
        List<Order> allOrders = getAllAssignedOrders(); // Assume getAllOrders() deserializzi gli ordini da un file
        OrderList confirmedDeliveries = new OrderList();

        for (Order order : allOrders) {
            if (order.getRiderId() == riderId && "consegnato".equals(order.getStatus())) {
                confirmedDeliveries.addOrder(order);
            }
        }

        return confirmedDeliveries;
    }


    // metodo verifica se c'è almeno un ordine in consegna assegnato a un rider specifico
    public boolean checkForOrderInDelivery(int riderId) {
        List<Order> allOrders = getAllAssignedOrders(); //  getAllOrders() anddava a deserializzare gli ordini da un file

        for (Order order : allOrders) {
            System.out.println("orderId: " + order.getOrderId() + "order.getRiderId: " + order.getRiderId() + " order.getStatus. " + order.getStatus());
            if (order.getRiderId() == riderId && "in consegna".equals(order.getStatus())) {
                return true; // C'è almeno un ordine in consegna per questo rider
            }
        }

        return false; // Nessun ordine in consegna trovato per riderid passato
    }


    public Order getInDeliveryOrderForRider(int riderId) throws OrderNotFoundException {
        List<Order> allOrders = getAllAssignedOrders();

        for (Order order : allOrders) {
            if (order.getRiderId() == riderId && "in consegna".equals(order.getStatus())) {
                return order; // Restituisce il primo ordine "in consegna" trovato per il rider
            }
        }

        // Se nessun ordine "in consegna" viene trovato per il rider -> eccezione
        throw new OrderNotFoundException("Nessun ordine 'in consegna' trovato per il rider con ID: " + riderId);
    }

    public void updateOrderStatusToDelivered(int orderId, LocalDateTime deliveryTime) {
        List<Order> allOrders = getAllAssignedOrders();
        boolean orderFound = false;

        for (Order order : allOrders) {
            if (order.getOrderId() == orderId) {
                order.setStatus("consegnato");
                order.setDeliveryTime(deliveryTime);
                orderFound = true;
                break;
            }
        }

        if (orderFound) {
            try {
                writeAssignedOrdersToFile(allOrders);
            }
            catch (IOException e){
                System.err.println("errore nella scrittura sul file degli ordini assegnati");

            }// Salvo la lista aggiornata degli ordini assegnati sul file
        } else {
            System.out.println("Ordine con ID " + orderId + " non trovato.");
        }
    }








}
