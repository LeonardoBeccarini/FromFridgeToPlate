package com.example.fromfridgetoplate.patterns.abstract_factory;

import com.example.fromfridgetoplate.logic.dao.*;

public interface DAOAbsFactory {

    ResellerDAO createResellerDAO();
    RiderDAO createRiderDAO();
    OrderDAO createOrderDAO();

    UserDAO createUserDAO();

    ShopDAO createShopDAO();
    CatalogDAO createCatalogDAO();

    ClientDAO createClientDAO();
}