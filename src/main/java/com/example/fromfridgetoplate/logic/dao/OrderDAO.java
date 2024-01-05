package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.model.Food_item;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.patterns.factory.BaseDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// public class OrderDAO implements BaseDAO<Void, OrderList> {
public class OrderDAO {

    private Connection connection;

    public OrderDAO(Connection connection) {
        this.connection = connection;
    }

    //public void set(Void p) {};

    public OrderList getPendingOrders() {
        OrderList orderList = new OrderList();
        CallableStatement cstmt = null;
        ResultSet rs = null;
        int order_id;

        try {
            cstmt = connection.prepareCall("{CALL GetPendingOrders()}");
            rs = cstmt.executeQuery();

            while (rs.next()) {

                order_id = rs.getInt("orderId");

                Order order = new Order(
                        order_id,
                        rs.getInt("CustomerId"),
                        rs.getString("NegozioId"),
                        "pronto",
                        new ArrayList<>(), //  gli 'Item' saramno gestiti poi in modo separato
                        rs.getTimestamp("orderTime").toLocalDateTime(),
                        //rs.getTimestamp("deliveryTime").toLocalDateTime(), da impostare in seguito, non al momento della creazione
                        rs.getString("shippingStreet"),
                        rs.getInt("shippingStreetNumber"),
                        rs.getString("shippingCity"),
                        rs.getString("shippingProvince"));

                order.setStatus(rs.getString("status")); // questo serve solo per vedere se prende gli ordini pronti
                order.setIsAcceptedByRider(rs.getBoolean("isAcceptedByRider")); // da moddare
                // Impostare altri campi x futuro se serve

                // Carica gli ingredienti per l'ordine
                loadOrderItems(order_id, order);
                orderList.addOrder(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione delle eccezioni
        } finally {

            closeQuietly(rs);
            closeQuietly(cstmt);
        }

        return orderList;
    }


    // con questo metodo dalla tabella Formazione nel db estraggo i foodItem( nel db corrispondo a "ingrediente" e li setto
    // nell'ordine corrispettivo, che sarà passato come parametro attuale
    private void loadOrderItems(int orderId, Order order) {
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            cstmt = connection.prepareCall("{CALL GetOrderItems(?)}");
            cstmt.setInt(1, orderId);
            rs = cstmt.executeQuery();

            List<Food_item> items = new ArrayList<>();

            while (rs.next()) {
                Food_item item = new Food_item(
                        rs.getString("Ingrediente"),
                        rs.getDouble("Quantita")
                );
                items.add(item);
            }

            order.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione delle eccezioni
        } finally {
            // Chiudi CallableStatement e ResultSet in modo sicuro
            closeQuietly(rs);
            closeQuietly(cstmt);
        }
    }




    public void update_availability(OrderBean orderBean) {
        CallableStatement cstmt = null;

        try {
            cstmt = connection.prepareCall("{CALL UpdateOrderStatus(?)}");
            cstmt.setInt(1, orderBean.getOrderId());
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione delle eccezioni
        } finally {
            closeQuietly(cstmt);
        }
    }





    // Metodo per stampare le informazioni dell'ordine a schermo, solo per vedere se le retrieva corretly
    public void printOrders(OrderList orderList) {
        for (Order order : orderList.getOrders()) {
            System.out.println("OrderId: " + order.getOrderId() +
                    ", RivenditaId: " + order.getRetailerId() +
                    ", CustomerId: " + order.getCustomerId() +
                    ", Status: " + order.getStatus() +
                    ", OrderTime: " + order.getOrderTime() +
                    ", DeliveryTime: " + order.getDeliveryTime() +
                    ", IsAcceptedByRider: " + order.isAcceptedByRider());

            // Stampa gli ingredienti alimentari per l'ordine
            System.out.println("Food Items:");
            for (Food_item item : order.getItems()) {
                System.out.println(" - Name: " + item.getName() + ", Quantity: " + item.getQuantity());
            }
            System.out.println("-------------------------------------");
        }
    }


    // Metodo dal china
    private void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Log dell'eccezione silenziosa se necessario
                e.printStackTrace();
            }
        }
    }
}
