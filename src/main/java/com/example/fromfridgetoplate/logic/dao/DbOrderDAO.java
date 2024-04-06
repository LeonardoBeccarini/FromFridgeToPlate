package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DeliveryRetrievalException;
import com.example.fromfridgetoplate.logic.exceptions.OrderNotFoundException;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;

import java.sql.*;
import java.time.LocalDateTime;

public class DbOrderDAO implements OrderDAO {


    private Connection connection;

    public DbOrderDAO(Connection connection) {
        this.connection = connection;
    }


    // trasferirsco in OrderDAO
    public void acceptOrder(int orderId, int riderId) {
        String query = "{CALL AcceptOrder(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, orderId);
            cstmt.setInt(2, riderId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // da moddare bene poi
        }

    }

    // trasferirsco in OrderDAO
    public void declineOrder(int orderId, int riderId) {
        String query = "{CALL DeclineOrder(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, orderId);
            cstmt.setInt(2, riderId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // da moddare bene poi
        }
    }

    // trasferirsco in OrderDAO
    public OrderList getConfirmedDeliveriesForRider(int riderId) throws DeliveryRetrievalException {
        OrderList confirmedDeliveries = new OrderList();

        String call = "{CALL GetConfirmedDeliveriesForRider(?)}";
        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setInt(1, riderId);

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
                    order.setDeliveryTime(rs.getTimestamp("deliveryTime").toLocalDateTime());
                    order.setShippingStreet(rs.getString("shippingStreet"));
                    order.setShippingStreetNumber(rs.getInt("shippingStreetNumber"));
                    order.setShippingCity(rs.getString("shippingCity"));
                    order.setShippingProvince(rs.getString("shippingProvince"));
                    // Impostare altre proprietà di Order come necessario...

                    confirmedDeliveries.addOrder(order);
                }
            }
        } catch (SQLException e) {
            throw new DeliveryRetrievalException("Errore nel recupero delle consegne confermate per il rider con ID: " + riderId, e);
        }

        return confirmedDeliveries;
    }

    // trasferirsco in OrderDAO
    public boolean checkForOrderInDelivery(int riderId) {
        String query = "{CALL checkOrderInDeliveryForRider(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderId);
            cstmt.registerOutParameter(2, Types.BOOLEAN);
            cstmt.execute();

            return cstmt.getBoolean(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // trasferirsco in OrderDAO
    public Order getInDeliveryOrderForRider(int riderId) throws OrderNotFoundException {
        Order order = null;
        String query = "{CALL GetInDeliveryOrderForRider(?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderId);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order(rs.getInt("orderId"), rs.getString("shippingStreet"), rs.getInt("shippingStreetNumber"), rs.getString("shippingCity"), rs.getString("shippingProvince"));
                } else {
                    // perchèse il resultSet è vuoto, significa che non è stato trovato alcun ordine 'in consegna' per il rider
                    throw new OrderNotFoundException("Nessun ordine 'in consegna' trovato");
                }
            }
        } catch (SQLException e) {

            throw new OrderNotFoundException("Errore nel recupero dell'ordine");
        }
        return order;
    }

    // trasferirsco in OrderDAO
    public void updateOrderStatusToDelivered(int orderId, LocalDateTime deliveryTime) {
        String query = "{CALL UpdateOrderToDelivered(?, ?)}";
        CallableStatement cstmt = null;  // metto qui null perche per il blocco finally, cstmt deve risultare inizializzato, e il blocco finally devo farlo perchè devo sempre chiudere la risorsa
        try {
            cstmt = connection.prepareCall(query);
            cstmt.setInt(1, orderId);
            cstmt.setTimestamp(2, Timestamp.valueOf(deliveryTime));
            cstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // chiudo risorse aperte
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
