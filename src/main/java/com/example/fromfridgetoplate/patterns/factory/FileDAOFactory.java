package com.example.fromfridgetoplate.patterns.factory;

import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOAbsFactory;

public class FileDAOFactory implements DAOAbsFactory {


    @Override
    public RiderDAO createRiderDAO() {
        return new FileRiderDAO();
    }

    @Override
    public OrderDAO createOrderDAO() {
        return new FileOrderDAO();
    }

    @Override
    public ResellerDAO createResellerDAO(){
        return new FileResellerDAO();
    }

    @Override
    public UserDAO createUserDAO() {return new FileUserDAO();}

    @Override
    public ShopDAO createShopDAO() {return new FileShopDAO();}

    @Override
    public CatalogDAO createCatalogDAO(){return new CatalogDAOImplFile();}
}