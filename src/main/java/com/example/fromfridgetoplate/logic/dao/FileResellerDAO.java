package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.OrderAssignmentException;
import com.example.fromfridgetoplate.logic.model.*;

import java.io.*;

import java.util.ArrayList;

import java.util.List;




public class FileResellerDAO extends FileDAOBase implements ResellerDAO {


    public FileResellerDAO() {
        super();

    }

    public OrderList getPendingOrders(String loggedEmail) throws DAOException {
        // Prima recupero tutti i negozi per trovare il VATnumber corrispondente all'email del reseller
        List<Shop> shops = getAllShops();

        String shopVATnumber = null;
        for (Shop shop : shops) {
            if (shop.getEmail().equals(loggedEmail)) {
                shopVATnumber = shop.getVATnumber();
                break;
            }
        }

        if (shopVATnumber == null) {

            return new OrderList(); // Nessun negozio trovato, quindi nessun ordine da restituire.
        }

        // Ora filtra gli ordini per lo status "pronto" e il VATnumber del negozio
        List<Order> allOrders = getAllOrders();
        OrderList pendingOrders = new OrderList();

        for (Order order : allOrders) {
            if ("pronto".equals(order.getStatus()) && shopVATnumber.equals(order.getShopId())) {
                pendingOrders.addOrder(order);
            }
        }

        return pendingOrders;
    }


    public void updateAvailability(OrderBean orderBean) throws DAOException  {
        List<Order> orders = getAllOrders();

        // Trovo l'ordine con l'ID specificato e aggiorno il suo stato
        for (Order order : orders) {
            if (order.getOrderId() == orderBean.getOrderId()) {
                order.setStatus("in consegna");
                break;
            }
        }

        // Sserializzo la lista aggiornata degli ordini nel file
        writeOrdersToFile(orders);
    }


    public void setAssignation(int orderId) throws OrderAssignmentException, DAOException {
        List<Order> orders = getAllOrders();
        boolean orderFound = false;

        // Trova l'ordine con l'ID specificato e aggiorna il suo stato
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                order.setStatus("in consegna"); // Aggiorno lo stato dell'ordine a "in consegna"
                try {
                    writeAssignedOrder(order); // Scrivo l'ordine aggiornato nel file degli `OrdiniAssegnati`
                } catch (IOException e) {
                    throw new OrderAssignmentException("Failed to write assigned order to file", e);
                }
                orderFound = true;
                break;
            }
        }

        if (orderFound) {
            // Aggiorno la lista degli ordini e la riscrivo nel file degli `Ordini`
            writeOrdersToFile(orders);
        }
    }


    private void writeAssignedOrder(Order assignedOrder) throws IOException, DAOException {


        List<Order> assignedOrders = getAllAssignedOrders();

        assignedOrder.setStatus("assegnato"); // vedi procedura sql "MoveOrderToAssigned"

        // Cerca se l'ordine esiste già tra quelli assegnati (basato sull'orderId)
        int index = -1;
        for (int i = 0; i < assignedOrders.size(); i++) {
            if (assignedOrders.get(i).getOrderId() == assignedOrder.getOrderId()) {
                index = i;
                break;
            }
        }

        // Se l'ordine esiste, aggiorno l'ordine esistente, altrimenti aggiungo il nuovo ordine
        if (index != -1) {
            assignedOrders.set(index, assignedOrder);
        } else {
            assignedOrders.add(assignedOrder);
        }

        // Riserializzo la lista aggiornata degli ordini assegnati nel file
        writeAssignedOrdersToFile(assignedOrders);
    }







    public OrderList getAssignedOrders(String currentResellerEmail) throws DAOException {

        List<Shop> allShops = getAllShops();
        List<Order> allAssignedOrders = getAllAssignedOrders();


        // Filtra i negozi per l'email corrente
        List<String> shopIds = new ArrayList<>();
        for (Shop shop : allShops) {

            if (shop.getEmail().equals(currentResellerEmail)) {

                shopIds.add(shop.getVATnumber());

            }
        }


        OrderList assignedOrderList = new OrderList();

        // Aggiungo alla lista ordini assegnati solo gli ordini che corrispondono al negozio filtrato
        for (Order order : allAssignedOrders) {

            if (shopIds.contains(order.getShopId())) {

                assignedOrderList.addOrder(order);
            }
        }

        return assignedOrderList;
    }

    private List<Shop> getAllShops() throws DAOException {

        return readFromFile(shopsFilePath);
    }


    public void assignRiderToOrder(int orderId, int riderId) throws DAOException  {
        List<Order> orders = getAllOrders();
        boolean isOrderFound = false;

        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                order.setRiderId(riderId); // Aggiorno l'ID del rider per l'ordine specificato
                isOrderFound = true;
                break;
            }
        }

        if (isOrderFound) {
            writeOrdersToFile(orders); // Riscrivo la lista aggiornata degli ordini nel file
        } else {
        }
    }


    public boolean isRiderAvailable(RiderBean riderBn) throws DAOException {
        List<Rider> riders = getAllRiders();

        for (Rider rider : riders) {
            if (rider.getId() == riderBn.getId()) {
                return rider.isAvailable();
            }
        }

        return false; // Restituisco false se non viene trovato nessun rider con l'ID specificato
    }


    public List<Rider> getAvailableRiders(SearchBean rpBean) throws DAOException {
        List<Rider> allRiders = getAllRiders();
        List<Rider> availableRiders = new ArrayList<>();

        for (Rider rider : allRiders) {
            if (rider.isAvailable() && rider.getAssignedCity().equalsIgnoreCase(rpBean.getCity())) {
                availableRiders.add(rider); // Aggiungo il rider alla lista se è disponibile e opera nella città specificata
            }
        }

        return availableRiders;
    }





}







