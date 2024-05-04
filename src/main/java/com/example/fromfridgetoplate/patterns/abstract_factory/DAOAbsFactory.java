package com.example.fromfridgetoplate.patterns.abstract_factory;

import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;

public interface DAOAbsFactory {

    ResellerDAO createResellerDAO() throws ConfigurationException;
    RiderDAO createRiderDAO() throws ConfigurationException;
    OrderDAO createOrderDAO() throws ConfigurationException;

    UserDAO createUserDAO() throws ConfigurationException;

    ShopDAO createShopDAO() throws ConfigurationException;
    CatalogDAO createCatalogDAO() throws ConfigurationException;

    ClientDAO createClientDAO() throws ConfigurationException;
}
