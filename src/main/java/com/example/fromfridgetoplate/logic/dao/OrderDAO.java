package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DeliveryRetrievalException;
import com.example.fromfridgetoplate.logic.exceptions.OrderNotFoundException;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;

import java.io.IOException;
import java.time.LocalDateTime;

public interface OrderDAO {

    void acceptOrder(int orderId, int riderId);

    void declineOrder(int orderId, int riderId) throws IOException;

    OrderList getConfirmedDeliveriesForRider(int riderId) throws DeliveryRetrievalException;

    boolean checkForOrderInDelivery(int riderId);

    Order getInDeliveryOrderForRider(int riderId) throws OrderNotFoundException;

    void updateOrderStatusToDelivered(int orderId, LocalDateTime deliveryTime);
}
