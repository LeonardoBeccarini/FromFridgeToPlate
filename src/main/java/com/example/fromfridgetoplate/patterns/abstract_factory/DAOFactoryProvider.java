package com.example.fromfridgetoplate.patterns.abstract_factory;

import com.example.fromfridgetoplate.logic.dao.PersistenceType;
import com.example.fromfridgetoplate.patterns.factory.DbDAOFactory;
import com.example.fromfridgetoplate.patterns.factory.FileDAOFactory;

// Centralizzo in questa classe la scelta di usare come strato di persistenza il db o il filesystem, in modo che il
// meccanismo di scelta non sia sparso nelle classi
// ma sia centralizzato solo in questa classe, che ha la responsabilità di creator sulle implementazioni concrete della
// abstactFactory, in base alla scelta che risiede in questa classe, creerà


// impl Singleton
public class DAOFactoryProvider {
    private static DAOFactoryProvider instance;
    private DAOAbsFactory daoFactory;

    private PersistenceType type = PersistenceType.JDBC;
    private DAOFactoryProvider() {

        // basta cambiare new DbDAOFactory(); con new FileDAOFactory per avere la versione basata su file, e lasciare
        // tutto il resto del codice uguale
         // o PersistenceType.JDBC, questo poi potrebbe essere letto da un file
        // di config oppure potrebbe essere scelto inizialmente dall' utente sulla gui

        if (type == PersistenceType.FILE_SYSTEM) {
            daoFactory = new FileDAOFactory();

        } else if (type == PersistenceType.JDBC) {
            daoFactory = new DbDAOFactory();

        }

    }


    public static DAOFactoryProvider getInstance() {
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


