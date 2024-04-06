package com.example.fromfridgetoplate.patterns.factory;

import com.example.fromfridgetoplate.logic.dao.*;

import java.sql.Connection;
//
public class DAOFactory { // rappresenta la factory della struttura nelle slide

//  ----------------------------------------DEPRECATED---------------------------------------------
        private Connection getConnection() {
            return SingletonConnector.getInstance().getConnection();
        }
        public DbResellerDAO getResellerDAO() {
            return new DbResellerDAO(getConnection());
        }

        public DbRiderDAO getRiderDAO() {
        return new DbRiderDAO(getConnection());
    }

        public DbOrderDAO getOrderDAO() {
        return new DbOrderDAO(getConnection());
    }

        public NotificationDAO getNotificationDAO() {return new NotificationDAO(getConnection());}

        public DbShopDAO getShopDAO() {return new DbShopDAO(getConnection());}





        // Altri metodi per gli altri DAo
}
