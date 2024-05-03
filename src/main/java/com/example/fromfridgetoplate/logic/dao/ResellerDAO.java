package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.OrderAssignmentException;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.logic.model.Rider;

import java.util.List;

public interface ResellerDAO {

    OrderList getPendingOrders(String email) throws DAOException;


    void setAssignation(int orderId) throws OrderAssignmentException, DAOException;

    OrderList getAssignedOrders(String currentResellerEmail) throws DAOException;

    void assignRiderToOrder(int orderId, int riderId) throws DAOException ;

    boolean isRiderAvailable(RiderBean riderBn) throws DAOException ;

    List<Rider> getAvailableRiders(SearchBean rpBean) throws DAOException;


}
