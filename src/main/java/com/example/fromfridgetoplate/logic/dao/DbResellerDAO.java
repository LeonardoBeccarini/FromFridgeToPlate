package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.CartItem;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.logic.model.Rider;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DbResellerDAO implements ResellerDAO{

    private Connection connection;

    public DbResellerDAO(Connection connection) {
        this.connection = connection;
    }


    public OrderList getPendingOrders(String loggedEmail) throws DAOException {  // passo l'email del reseller attualmente loggato
        OrderList orderList = new OrderList();
        CallableStatement cstmt = null;
        ResultSet rs = null;
        int orderId;

        try {
            cstmt = connection.prepareCall("{CALL GetPendingOrders(?)}");
            cstmt.setString(1, loggedEmail);
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

            throw new DAOException("Failed to retrieve pending orders for email: " + loggedEmail, e);

        } finally {

            close(rs);
            close(cstmt);
        }

        return orderList;
    }


    // con questo metodo dalla tabella Formazione nel db estraggo i foodItem( nel db corrispondo a "ingrediente" e li setto
    // nell'ordine corrispettivo, che sarà passato come parametro attuale
    private void loadOrderItems(int orderId, Order order) throws DAOException {
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
            throw new DAOException("Errore nel caricare gli order items per l'ordine con order ID: " + orderId, e);

        } finally {
            // Chiudo le risosre CallableStatement e ResultSet in modo sicuro
            close(rs);
            close(cstmt);
        }
    }


    public void updateAvailability(OrderBean orderBean) throws DAOException {
        CallableStatement cstmt = null;

        try {
            cstmt = connection.prepareCall("{CALL UpdateOrderStatus(?)}");
            cstmt.setInt(1, orderBean.getOrderId());
            cstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Failed to update availability for order ID: " + orderBean.getOrderId(), e);
        } finally {
            close(cstmt);
        }
    } // not used


    // Metodo dal china
    // Metodo per chiudere risorse con gestione centralizzata delle eccezioni
    private void close(AutoCloseable resource) throws DAOException {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Lancio una DAOException che avvolge l'eccezione originale
                throw new DAOException("Errore nella chiusura delle risorse", e);
            }
        }
    }


    public void setAssignation(int orderId) throws DAOException {
        String query = "{CALL MoveOrderToAssigned(?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, orderId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Failed to assign order", e);
        }
    }



    public OrderList getAssignedOrders(String currentResellerEmail) throws DAOException {
        OrderList orderList = new OrderList();
        try (CallableStatement cstmt = connection.prepareCall("{CALL GetAssignedOrders(?)}")) {
            cstmt.setString(1, currentResellerEmail);
            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("orderId"),
                            rs.getString("CustomerId"),
                            rs.getString("NegozioId"),
                            rs.getString("status"),
                            rs.getTimestamp("orderTime").toLocalDateTime(),
                            rs.getInt("RiderId")
                    );
                    order.setShippingCity(rs.getString("shippingCity"));
                    orderList.addOrder(order);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to fetch assigned orders", e);
        }
        return orderList;
    }



    public void assignRiderToOrder(int orderId, int riderId) throws DAOException {

        CallableStatement stmt = null;

        try {

            stmt = connection.prepareCall("{CALL AssignRiderToOrder(?, ?)}");
            stmt.setInt(1, orderId);
            stmt.setInt(2, riderId);

            // Esecuzione della stored procedure
            stmt.execute();
        } catch (SQLException e) {
            throw new DAOException("Failed to assign rider to order", e);
        } finally {
            close(stmt);

        }
    }

    public boolean isRiderAvailable(RiderBean riderBn) throws DAOException {
        String query = "{CALL GetRiderAvailability(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderBn.getId());
            cstmt.registerOutParameter(2, Types.BOOLEAN);
            cstmt.execute();

            return cstmt.getBoolean(2);
        } catch (SQLException e) {
            throw new DAOException("Errore nel check della disponibilità del rider", e);
        }
    }


    // Metodo per ottenere i rider disponibili
    public List<Rider> getAvailableRiders(SearchBean rpBean) throws DAOException {
        List<Rider> availableRiders = new ArrayList<>();
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            cstmt = connection.prepareCall("{CALL GetAvailableRiders(?)}");// la stored procedure ritornerà un result_set con
            // i riders operanti in quella città(indiciata da pBean.getCity())
            cstmt.setString(1, rpBean.getCity());
            rs = cstmt.executeQuery();

            while (rs.next()) {
                int riderId = rs.getInt("Id");
                Rider rider = new Rider(
                        riderId,
                        rs.getString("Nome"),
                        rs.getString("Cognome"),
                        rs.getString("assignedCity")

                );
                availableRiders.add(rider);
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel retrieve dei rider disponibili", e);

        } finally {

            close(rs);
            close(cstmt);
        }

        return availableRiders;
    }

}