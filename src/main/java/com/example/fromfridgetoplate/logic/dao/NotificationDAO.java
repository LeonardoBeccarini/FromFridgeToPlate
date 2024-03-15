package com.example.fromfridgetoplate.logic.dao;


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


    public void insertNotification(int riderId, Order order, String message )
    {
        String query = "{CALL insertNotification(?, ?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderId);
            cstmt.setInt(2, order.getOrderId());
            cstmt.setString(3,null);        // perchè ho aggiunto customerId alla notifica
            // bastava mettere dentro a notification un'istanza dell'entità order
            cstmt.setString(4,null);        // perchè ho aggiunto shopId alla notifica
            cstmt.setString(3, order.getShippingStreet());

            cstmt.setInt(4, order.getShippingStreetNumber());
            cstmt.setString(5, order.getShippingCity());
            cstmt.setString(6, order.getShippingProvince());
            cstmt.setString(7, message);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    public void insertNotificationRes(Order order, String message){
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
            e.printStackTrace();

        }
    }


    public List<Notification> getNotificationsForRider(int riderId, int lastDeliveredNotificationId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "{CALL getNotificationsForRider(?, ?)}";

        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderId);
            cstmt.setInt(2, lastDeliveredNotificationId);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification(
                            rs.getInt("riderId"),
                            rs.getInt("orderId"),
                            rs.getString("shippingStreet"),
                            rs.getInt("shippingStreetNumber"),
                            rs.getString("shippingCity"),
                            rs.getString("shippingProvince"),
                            rs.getString("message")
                    );
                    notification.setNotificationId(rs.getInt("notificationId"));
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            //
        }
        return notifications;
    }

    public List<Notification> getNotificationsForOwner(String vatNumber) {
        List<Notification> notifications = new ArrayList<>();
        String query = "{CALL getNotificationsForOwner(?)}";

        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setString(1, vatNumber);
            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification(
                            rs.getString("customerId"),
                            rs.getInt("orderId"),
                            rs.getString("shippingStreet"),
                            rs.getInt("shippingStreetNumber"),
                            rs.getString("shippingCity"),
                            rs.getString("shippingProvince"),
                            rs.getString("message")
                    );
                    notification.setNotificationId(rs.getInt("notificationId"));
                    Utils.print("dal db id della notifica" + " " + notification.getNotificationId());
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            //
        }
        return notifications;
    }


    public void markNotificationAsRead(int notificationId) {
        String query = "{CALL SetNotificationAsRead(?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, notificationId);
            Utils.print("sono dentro al blocco try di markNotificationAsRead" + notificationId);
           cstmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }












}

