package com.example.fromfridgetoplate.patterns.abstractFactory;


import com.example.fromfridgetoplate.logic.dao.*;

public interface DAOAbsFactory {

    ResellerDAO createResellerDAO();
    RiderDAO createRiderDAO();
    OrderDAO createOrderDAO();

    UserDAO createUserDAO();

    ShopDAO createShopDAO();
}
