package com.example.fromfridgetoplate.patterns.abstract_factory;

import com.example.fromfridgetoplate.logic.dao.PersistenceType;
import com.example.fromfridgetoplate.patterns.factory.DbDAOFactory;
import com.example.fromfridgetoplate.patterns.factory.FileDAOFactory;

// Centralizzo in questa classe la scelta di usare come strato di persistenza il db o il filesystem, in modo che il
// meccanismo di scelta non sia sparso nelle classi, come ad esempio :
// DAOAbsFactory daoAbsFactory = new FileDAOFactory();
// ShopDAO shopDAO = daoAbsFactory.createShopDAO();
// ma sia centralizzato solo in questa classe, che ha la responsabilità di creator sulle implementazioni concrete della
// abstactFactory, in base alla scelta che risiede in questa classe, creerà




// impl Singleton


import com.example.fromfridgetoplate.logic.dao.PersistenceType;
import com.example.fromfridgetoplate.patterns.factory.DbDAOFactory;
import com.example.fromfridgetoplate.patterns.factory.FileDAOFactory;

public class DAOFactoryProvider {
    private static DAOFactoryProvider instance;
    private DAOAbsFactory daoFactory;
    private PersistenceType type = PersistenceType.FILE_SYSTEM;

    private DAOFactoryProvider() {
        initializeFactory();
    }

    private void initializeFactory() {
        if (type == PersistenceType.FILE_SYSTEM) {
            daoFactory = new FileDAOFactory();
        } else if (type == PersistenceType.JDBC) {
            daoFactory = new DbDAOFactory();
        }
    }

    public static synchronized DAOFactoryProvider getInstance() {
        if (instance == null) {
            instance = new DAOFactoryProvider();
        }
        return instance;
    }

    public PersistenceType getType() {
        return type;
    }

    public DAOAbsFactory getDaoFactory() {
        return daoFactory;
    }
}




