package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.CartItem;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderDAO {

    private Connection connection;

    public OrderDAO(Connection connection) {
        this.connection = connection;
    }


    public OrderList getPendingOrders() {
        OrderList orderList = new OrderList();
        CallableStatement cstmt = null;
        ResultSet rs = null;
        int orderId;

        try {
            cstmt = connection.prepareCall("{CALL GetPendingOrders()}");
            rs = cstmt.executeQuery();

            while (rs.next()) {

                orderId = rs.getInt("orderId");

                Order order = new Order(
                        orderId,
                        rs.getString("CustomerId"),
                        rs.getString("NegozioId"),
                        "pronto",
                        new ArrayList<>(),
                        rs.getTimestamp("orderTime").toLocalDateTime()
                );


                order.setShippingStreet(rs.getString("shippingStreet"));
                order.setShippingStreetNumber(rs.getInt("shippingStreetNumber"));
                order.setShippingCity(rs.getString("shippingCity"));
                order.setShippingProvince(rs.getString("shippingProvince"));
                order.setStatus(rs.getString("status")); // questo serve solo per vedere se prende gli ordini pronti
                order.setIsAcceptedByRider(rs.getBoolean("isAcceptedByRider")); // da moddare
                // Impostare altri campi x futuro se serve

                // Carica gli ingredienti per l'ordine
                loadOrderItems(orderId, order);
                orderList.addOrder(order);
            }
        } catch (SQLException e) {

            e.printStackTrace();

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

            List<CartItem> items = new ArrayList<>();

            while (rs.next()) {
                CartItem item = new CartItem(
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


    public void updateAvailability(OrderBean orderBean) {
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

    public void setAssignation(int orderId) {
        String query = "{CALL MoveOrderToAssigned(?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, orderId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestisci l'eccezione
        }
    }


    public OrderList getAssignedOrders(String currentRiderEmail) {

        OrderList orderList = new OrderList();
        try (CallableStatement cstmt = connection.prepareCall("{CALL GetAssignedOrders(?)}")) {
            cstmt.setString(1, currentRiderEmail);

            try (ResultSet rs = cstmt.executeQuery()) {

                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("orderId"),
                            rs.getString("CustomerId"),
                            rs.getString("NegozioId"), // questo corrisponderà al vatnumber dello specifico reseller che è attualmente loggato
                            rs.getString("status"),
                            rs.getTimestamp("orderTime").toLocalDateTime(),
                            rs.getInt("RiderId")
                    );
                    String city = rs.getString("shippingCity");
                    order.setShippingCity(city);
                    orderList.addOrder(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return orderList;
    }


    public boolean assignRiderToOrder(int orderId, int riderId) {

        CallableStatement stmt = null;

        try {

            stmt = connection.prepareCall("{CALL AssignRiderToOrder(?, ?)}");
            stmt.setInt(1, orderId);
            stmt.setInt(2, riderId);

            // Esecuzione della stored procedure
            stmt.execute();
            return true; // L'aggiornamento ha avuto successo
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // L'aggiornamento non è riuscito
        } finally {
            // Chiudi le risorse JDBC in modo sicuro
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



// ------------------- BECCA ---------------------- BECCA ---------------------- BECCA ---------------------- //

    public Order saveOrder(Order order) throws DbException {
        int orderID;
        List<CartItem> cartItemList= order.getItems();
        try(CallableStatement cs = connection.prepareCall("{CALL saveOrder(?,?,?,?,?,?,?)}")){
            cs.setString(1, order.getShopId());
            cs.setString(2, order.getCustomerId());
            cs.setString(3, order.getShippingStreet());
            cs.setInt(4, order.getShippingStreetNumber());
            cs.setString(5, order.getShippingCity());
            cs.setString(6, order.getShippingProvince());
            cs.registerOutParameter(7, Types.NUMERIC);
            cs.executeQuery();
            orderID = cs.getInt(7);
        for(CartItem cartItem: cartItemList){

            try(CallableStatement cs2 = connection.prepareCall("{CALL addIngredientToOrder(?,?,?)}")){
                cs2.setInt(1, orderID);
                cs2.setString(2, cartItem.getName());
                cs2.setDouble(3, cartItem.getQuantity());
                cs2.executeQuery();
            }
        }

        }catch(SQLException e){
            e.printStackTrace();
            throw new DbException("errore database:"+" " + e.getMessage());
        }
       order.setOrderId(orderID);
        return order;
    }

}