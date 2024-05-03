package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.exceptions.OrderAssignmentException;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.logic.model.Rider;

import java.io.IOException;
import java.util.List;

public interface ResellerDAO {

    OrderList getPendingOrders(String email);

    void updateAvailability(OrderBean orderBean) ;

    void setAssignation(int orderId) throws OrderAssignmentException;

    OrderList getAssignedOrders(String currentResellerEmail);

    boolean assignRiderToOrder(int orderId, int riderId) ;

    boolean isRiderAvailable(RiderBean riderBn);

    List<Rider> getAvailableRiders(SearchBean rpBean);


}
