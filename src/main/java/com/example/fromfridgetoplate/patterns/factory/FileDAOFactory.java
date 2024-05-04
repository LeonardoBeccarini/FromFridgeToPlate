package com.example.fromfridgetoplate.patterns.factory;

import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOAbsFactory;



public class FileDAOFactory implements DAOAbsFactory {


    @Override
    public RiderDAO createRiderDAO() throws ConfigurationException {
        return new FileRiderDAO();
    }

    @Override
    public OrderDAO createOrderDAO() throws ConfigurationException {
        return new FileOrderDAO();
    }

    @Override
    public ResellerDAO createResellerDAO() throws ConfigurationException {
        return new FileResellerDAO();
    }

    @Override
    public UserDAO createUserDAO() throws ConfigurationException {return new FileUserDAO();}

    @Override
    public ShopDAO createShopDAO() throws ConfigurationException {return new FileShopDAO();}

    @Override
    public CatalogDAO createCatalogDAO() throws ConfigurationException {return new CatalogDAOImplFile();}

    @Override
    public ClientDAO createClientDAO() throws ConfigurationException {return new FileClientDAO();}
}