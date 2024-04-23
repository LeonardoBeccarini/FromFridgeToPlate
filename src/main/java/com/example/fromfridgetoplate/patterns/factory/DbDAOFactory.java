package com.example.fromfridgetoplate.patterns.factory;

import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOAbsFactory;

import java.sql.Connection;

public class DbDAOFactory implements DAOAbsFactory {
    private Connection connection;


    public DbDAOFactory() {
        this.connection = this.getConnection();
    }

    private Connection getConnection() {
        return SingletonConnector.getInstance().getConnection();
    }
    @Override
    public ResellerDAO createResellerDAO(){
        return new DbResellerDAO(connection);
    }
    @Override
    public RiderDAO createRiderDAO() {
        return new DbRiderDAO(connection);
    }

    @Override
    public OrderDAO createOrderDAO() {
        return new DbOrderDAO(connection);
    }

    @Override
    public UserDAO createUserDAO() {return new DbUserDAO();}

    @Override
    public ShopDAO createShopDAO() {return new DbShopDAO();}

    @Override
    public CatalogDAO createCatalogDAO(){return new CatalogDAOImplJDBC();}
}
