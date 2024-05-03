package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.exceptions.DeliveryRetrievalException;
import com.example.fromfridgetoplate.logic.exceptions.OrderNotFoundException;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;

import java.io.IOException;
import java.time.LocalDateTime;

public interface OrderDAO {

    void acceptOrder(int orderId, int riderId) throws DAOException;

    void declineOrder(int orderId, int riderId) throws DAOException;
    ;

    OrderList getConfirmedDeliveriesForRider(int riderId) throws DeliveryRetrievalException, DAOException;

    boolean checkForOrderInDelivery(int riderId) throws DAOException;

    Order getInDeliveryOrderForRider(int riderId) throws OrderNotFoundException, DAOException;

    void updateOrderStatusToDelivered(int orderId, LocalDateTime deliveryTime) throws DAOException;
    Order saveOrder(Order order) throws DbException, DAOException;
}
