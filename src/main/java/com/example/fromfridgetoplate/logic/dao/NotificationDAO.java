package com.example.fromfridgetoplate.logic.dao;


import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Notification;
import com.example.fromfridgetoplate.logic.model.Order;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class NotificationDAO {

    private Connection connection;


    public NotificationDAO(Connection conn) {
        this.connection = conn;
    }


    public void insertNotification(int riderId, Order order, String message ) throws DAOException {
        String query = "{CALL insertNotification(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderId);
            cstmt.setInt(2, order.getOrderId());
            cstmt.setString(3,null);        // perchè ho aggiunto customerId alla notifica
            // bastava mettere dentro a notification un'istanza dell'entità order
            cstmt.setString(4,null);        // perchè ho aggiunto shopId alla notifica
            cstmt.setString(5, order.getShippingStreet());

            cstmt.setInt(6, order.getShippingStreetNumber());
            cstmt.setString(7, order.getShippingCity());
            cstmt.setString(8, order.getShippingProvince());
            cstmt.setString(9, message);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore durante l'inserimento della notifica per il Rider rispetto all'ordine ID: " + order.getOrderId(), e);

        }
    }

    public void insertNotificationRes(Order order, String message) throws DAOException {
        String query = "{CALL insertNotification(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, 0);
            cstmt.setInt(2, order.getOrderId());
            cstmt.setString(3, order.getShopId());
            cstmt.setString(4, order.getCustomerId());
            cstmt.setString(5, order.getShippingStreet());
            cstmt.setInt(6, order.getShippingStreetNumber());
            cstmt.setString(7, order.getShippingCity());
            cstmt.setString(8, order.getShippingProvince());
            cstmt.setString(9, message);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore durante l'inserimento della notifica per il Reseller rispetto  all'ordine ID: " + order.getOrderId(), e);

        }
    }


    public List<Notification> getNotificationsForRider(int riderId, int lastDeliveredNotificationId) throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        String query = "{CALL getNotificationsForRider(?, ?)}";

        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderId);
            cstmt.setInt(2, lastDeliveredNotificationId);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("riderId"),
                            rs.getInt("orderId"),
                            rs.getString("shippingStreet"),
                            rs.getInt("shippingStreetNumber"),
                            rs.getString("shippingCity"),
                            rs.getString("shippingProvince")
                    );
                    Notification notification = new Notification(order,
                            rs.getString("message")
                    );
                    notification.setNotificationId(rs.getInt("notificationId"));
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero delle notifiche per il rider.", e);
        }
        return notifications;
    }

    public List<Notification> getNotificationsForOwner(String vatNumber) throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        String query = "{CALL getNotificationsForOwner(?)}";

        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setString(1, vatNumber);
            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    Order order =  new Order( rs.getString("customerId"),
                            rs.getInt("orderId"),
                            rs.getString("shippingStreet"),
                            rs.getInt("shippingStreetNumber"),
                            rs.getString("shippingCity"),
                            rs.getString("shippingProvince"));
                    Notification notification = new Notification(order,
                            rs.getString("message"));
                    notification.setNotificationId(rs.getInt("notificationId"));
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero delle notifiche per il Reseller.", e);
        }
        return notifications;
    }


    public void markNotificationAsRead(int notificationId) throws DAOException {
        String query = "{CALL SetNotificationAsRead(?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, notificationId);
           cstmt.executeQuery();

        } catch (SQLException e) {
            throw new DAOException("Errore nel marcare notifiche come lette: " + e.getMessage());

        }
    }

}

