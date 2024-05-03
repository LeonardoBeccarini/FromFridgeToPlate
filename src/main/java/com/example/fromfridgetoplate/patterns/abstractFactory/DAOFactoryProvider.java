package com.example.fromfridgetoplate.patterns.abstractFactory;

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
public class DAOFactoryProvider {
    private static class Holder {
        static final DAOFactoryProvider INSTANCE = new DAOFactoryProvider();
    }

    private DAOAbsFactory daoFactory;
    private PersistenceType type = PersistenceType.FILE_SYSTEM;

    private DAOFactoryProvider() {
        if (type == PersistenceType.FILE_SYSTEM) {
            daoFactory = new FileDAOFactory();
        } else if (type == PersistenceType.JDBC) {
            daoFactory = new DbDAOFactory();
        }
    }

    public static DAOFactoryProvider getInstance() {
        return Holder.INSTANCE;
    }

    public PersistenceType getType() {
        return type;
    }

    public DAOAbsFactory getDaoFactory() {
        return daoFactory;
    }
}



