package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.exceptions.DeliveryRetrievalException;
import com.example.fromfridgetoplate.logic.exceptions.OrderNotFoundException;
import com.example.fromfridgetoplate.logic.model.CartItem;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class DbOrderDAO implements OrderDAO {


    private Connection connection;

    public DbOrderDAO(Connection connection) {
        this.connection = connection;
    }


    // trasferirsco in OrderDAO
    public void acceptOrder(int orderId, int riderId) throws DAOException {
        String query = "{CALL AcceptOrder(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, orderId);
            cstmt.setInt(2, riderId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Operazione di accettazione dell'ordine nel DB fallita", e);
        }

    }

    // trasferirsco in OrderDAO
    public void declineOrder(int orderId, int riderId) throws DAOException {
        String query = "{CALL DeclineOrder(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, orderId);
            cstmt.setInt(2, riderId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("L'ordine " + orderId + " non è stato aggiornato.");
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
    @Override
    public void updateOrderStatusToDelivered(int orderId, LocalDateTime deliveryTime) throws DAOException {
        String query = "{CALL UpdateOrderToDelivered(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) { // uso try-with resources in modo che ogni risorsa (CallableStatement in questo caso) venga chiusa alla fine del blocco, cosi non serve finally, perchè ajva cosi garantisce che quella risorsa venga chiusa al termine del blocco try, indipendentemente dal fatto che l'esecuzione avvenga normalmente o che venga lanciata uneccezione
            cstmt.setInt(1, orderId);
            cstmt.setTimestamp(2, Timestamp.valueOf(deliveryTime));
            cstmt.execute();
        } catch (SQLException e) {
            throw new DAOException("Failed to update order status to delivered for order ID: " + orderId, e);
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

                try(CallableStatement cs2 = connection.prepareCall("{CALL addIngredientToOrder(?,?,?)}")){
                    cs2.setInt(1, orderID);
                    for(CartItem cartItem: cartItemList){
                        cs2.setString(2, cartItem.getName());
                        cs2.setDouble(3, cartItem.getQuantity());
                        cs2.addBatch();
                    }
                    cs2.executeBatch();
            }

        }catch(SQLException e){
            throw new DbException("errore database:"+" " + e.getMessage());
        }
        order.setOrderId(orderID);
        return order;
    }
}
