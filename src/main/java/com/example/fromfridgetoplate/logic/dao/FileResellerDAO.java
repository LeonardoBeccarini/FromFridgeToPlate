package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FileResellerDAO extends FileDAOBase implements ResellerDAO {


    public FileResellerDAO() {
        super();

    }

    public OrderList getPendingOrders(String loggedEmail) {
        // Prima recupera tutti i negozi per trovare il VATnumber corrispondente all'email del reseller
        List<Shop> shops = getAllShops();

        String shopVATnumber = null;
        for (Shop shop : shops) {
            if (shop.getEmail().equals(loggedEmail)) {
                shopVATnumber = shop.getVATnumber();
                break;
            }
        }

        if (shopVATnumber == null) {
            System.out.println("Negozio non trovato per l'email: " + loggedEmail);
            return new OrderList(); // Nessun negozio trovato, quindi nessun ordine da restituire.
        }

        // Ora filtra gli ordini per lo status "pronto" e il VATnumber del negozio
        List<Order> allOrders = getAllOrders(); // Assumi che questo metodo legga gli ordini da un file
        OrderList pendingOrders = new OrderList();

        for (Order order : allOrders) {
            if ("pronto".equals(order.getStatus()) && shopVATnumber.equals(order.getShopId())) {
                pendingOrders.addOrder(order);
            }
        }

        System.out.println("getPendingOrders called for shop VAT: " + shopVATnumber);
        return pendingOrders;
    }


    public void updateAvailability(OrderBean orderBean)  {
        List<Order> orders = getAllOrders();

        // Trovo l'ordine con l'ID specificato e aggiorna il suo stato
        for (Order order : orders) {
            if (order.getOrderId() == orderBean.getOrderId()) {
                order.setStatus("in consegna"); // Aggiorna lo stato dell'ordine a in consegna
                break;
            }
        }

        // Sserializzo la lista aggiornata degli ordini nel file
        writeOrdersToFile(orders);
    }


    public Order saveOrder(Order order)  {
        // Recupera tutti gli ordini esistenti e la mappa degli item associati
        List<Order> orders = getAllOrders();
        Map<Integer, List<CartItem>> orderItemsMap = getAllOrderItems();


        // Assegno un id univoco all'ordine
        int maxOrderId = 0; // Inizializzo l'ID massimo a 0

        for (Order existingOrder : orders) {
            if (existingOrder.getOrderId() > maxOrderId) {
                maxOrderId = existingOrder.getOrderId(); // Aggiorno l'ID massimo se trova un ordine con ID maggiore
                System.out.println("orderId: "+ maxOrderId + " status: " + existingOrder.getStatus());
            }
        }
        order.setOrderId(maxOrderId + 1); // Assegna al nuovo ordine l'ID successivo all'ID massimo trovato

        orders.add(order); // Aggiungo il nuovo ordine alla lista

        // Aggiungo gli item dell'ordine alla mappa
        orderItemsMap.put(order.getOrderId(), order.getItems());

        // Aggiorno il file degli ordini e la mappa degli item
        writeOrdersToFile(orders);
        try {
            writeOrderItemsMapToFile(orderItemsMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return order;
    }

    private void writeOrderItemsMapToFile(Map<Integer, List<CartItem>> orderItemsMap) throws IOException {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(properties.getProperty("orderItemsMapFilePath")))) {
            oos.writeObject(orderItemsMap);
        }
    }

    public Map<Integer, List<CartItem>> getAllOrderItems() {
        String itemsMapFileName = properties.getProperty("orderItemsMapFilePath");
        File file = new File(itemsMapFileName);
        if (!file.exists()) return new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<Integer, List<CartItem>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }





    public void setAssignation(int orderId) {
        List<Order> orders = getAllOrders();
        boolean orderFound = false;

        // Trova l'ordine con l'ID specificato e aggiorna il suo stato
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                order.setStatus("in consegna"); // Aggiorno lo stato dell'ordine a "in consegna"
                try {
                    writeAssignedOrder(order); // Scrivo l'ordine aggiornato nel file degli `OrdiniAssegnati`
                } catch (IOException e) {
                    throw new RuntimeException(e);
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


    private void writeAssignedOrder(Order assignedOrder) throws IOException {


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







    public OrderList getAssignedOrders(String currentResellerEmail) {
        System.out.println("currentResellerEmail: " + currentResellerEmail );
        List<Shop> allShops = getAllShops();
        List<Order> allAssignedOrders = getAllAssignedOrders();


        // Filtra i negozi per l'email corrente
        List<String> shopIds = new ArrayList<>();
        for (Shop shop : allShops) {
            System.out.println("ResellerEmail: " + shop.getEmail() );
            if (shop.getEmail().equals(currentResellerEmail)) {
                System.out.println("currentResellerVat: " + shop.getVATnumber() );
                shopIds.add(shop.getVATnumber());

            }
        }


        OrderList assignedOrderList = new OrderList();

        // Aggiungo alla lista ordini assegnati solo gli ordini che corrispondono al negozio filtrato
        for (Order order : allAssignedOrders) {
            System.out.println("shopid: " + order.getShopId() );
            if (shopIds.contains(order.getShopId())) {
                System.out.println("shopid check: " + order.getShopId() );
                assignedOrderList.addOrder(order);
            }
        }

        return assignedOrderList;
    }

    private List<Shop> getAllShops() {

        return readFromFile(shopsFilePath);
    }


    public boolean assignRiderToOrder(int orderId, int riderId)  {
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
            return true;
        } else {
            return false; // L'ordine specificato non è stato trovato
        }
    }


    public boolean isRiderAvailable(RiderBean riderBn) {
        List<Rider> riders = getAllRiders();

        for (Rider rider : riders) {
            if (rider.getId() == riderBn.getId()) {
                return rider.isAvailable();
            }
        }

        return false; // Restituisco false se non viene trovato nessun rider con l'ID specificato
    }


    public List<Rider> getAvailableRiders(SearchBean rpBean) {
        List<Rider> allRiders = getAllRiders();
        List<Rider> availableRiders = new ArrayList<>();

        for (Rider rider : allRiders) {
            if (rider.isAvailable() && rider.getAssignedCity().equalsIgnoreCase(rpBean.getCity())) {
                availableRiders.add(rider); // Aggiungo il rider alla lista se è disponibile e opera nella città specificata
            }
        }

        return availableRiders;
    }





    public static void main(String[] args) throws IOException {

        // Test getAllRiders
        FileResellerDAO dao = new FileResellerDAO();
        List<Rider> riders = dao.getAllRiders();
        System.out.println("Numero totale di rider estratti: " + riders.size());

        // Test saveOrder
        /*Order newOrder = new Order(
                0, // L'ID verrà assegnato automaticamente
                "Customer1", "Shop1", "pronto", new ArrayList<>(), LocalDateTime.now()
        );*/

        // Test saveOrder

        //  CartItem per il test
        CartItem item1 = new CartItem("Pane", 2);
        CartItem item2 = new CartItem("Latte", 4);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(item1);
        cartItems.add(item2);

        //  nuovo Order con gli item
        Order newOrder = new Order("vat12345678", "Customer1", "Via Roma", 12, "Milano", "MI");
        newOrder.setStatus("pronto");
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setItems(cartItems);


        Order savedOrder = dao.saveOrder(newOrder);
        System.out.println("Ordine salvato con ID: " + savedOrder.getOrderId());

        // Verifico se l'ordine è stato salvato correttamente e stampa i dettagli degli item associati
        Map<Integer, List<CartItem>> orderItemsMap = dao.getAllOrderItems();
        System.out.println("Dettagli degli ordini salvati:");
        orderItemsMap.forEach((orderId, items) -> {
            System.out.println("Ordine ID: " + orderId);
            items.forEach(item -> System.out.println(" - " + item.getName() + ", Quantità: " + item.getQuantity()));
        });



        // Test getPendingOrders
            // qui cambi mettendo quircio4@gmail.com o quircio5@gmail.com, devi cambiare anche sopra il vat del nuovo ordine se vuoi aggiungerne un nuovo ordine per quel dato shop pero
        OrderList pendingOrders = dao.getPendingOrders("quircio4@gmail.com");
        System.out.println("Ordini in sospeso: ");
        for (Order order : pendingOrders.getOrders()) {
            System.out.println("ID Ordine: " + order.getOrderId() + ", Stato: " + order.getStatus() + "shopId:" + order.getShopId());
        }


        // Assumendo che abbiamo almeno un ordine in sospeso da aggiornare
       /* if (!pendingOrders.getOrders().isEmpty()) {
            // Prendi l'ID del primo ordine in sospeso per la dimostrazione
            int orderIdToUpdate = pendingOrders.getOrders().get(0).getOrderId();
            System.out.println("valore di orderIdToUpdate: " + orderIdToUpdate);

            // Crea un OrderBean per simulare l'input dell'aggiornamento
            OrderBean orderBeanToUpdate = new OrderBean();
            orderBeanToUpdate.setOrderId(orderIdToUpdate);

            // Test updateAvailability
            dao.updateAvailability(orderBeanToUpdate);

            // Verifica se l'aggiornamento è stato eseguito correttamente
            OrderList updatedOrders = dao.getPendingOrders();
            System.out.println("Dopo l'aggiornamento, ordini in sospeso: ");
            for (Order order : updatedOrders.getOrders()) {
                System.out.println("ID Ordine: " + order.getOrderId() + ", Stato: " + order.getStatus());
            }
        } else {
            System.out.println("Nessun ordine in sospeso da aggiornare.");
        }*/





    }





}







