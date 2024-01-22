package com.example.fromfridgetoplate.patterns.factory;

import com.example.fromfridgetoplate.logic.dao.NotificationDAO;
import com.example.fromfridgetoplate.logic.dao.OrderDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.dao.SingletonConnector;

import java.sql.Connection;

public class DAOFactory { // rappresenta la factory della struttura nelle slide


        private Connection getConnection() {
            return SingletonConnector.getInstance().getConnection();
        }
        public  OrderDAO getOrderDAO() {
            return new OrderDAO(getConnection());
        }

        public RiderDAO getRiderDAO() {
        return new RiderDAO(getConnection());
    }

        public NotificationDAO getNotificationDAO() {return new NotificationDAO(getConnection());}




        // Altri metodi per gli altri DAo
}
