package com.example.fromfridgetoplate.patterns.factory;

import com.example.fromfridgetoplate.logic.dao.CatalogDAO;
import com.example.fromfridgetoplate.logic.dao.CatalogDAOImplFIle;
import com.example.fromfridgetoplate.logic.dao.CatalogDAOImplJDBC;
import com.example.fromfridgetoplate.logic.dao.PersistenceType;
import com.example.fromfridgetoplate.logic.exceptions.CatalogDAOFactoryError;

public class CatalogDAOFactory{

    public CatalogDAO createCatalogDAO(PersistenceType persistenceType) throws CatalogDAOFactoryError{
        // ritorna implementazioni diverse della stessa interfaccia a seconda del tipo di persistenza

        if(persistenceType == PersistenceType.JDBC){
            return new CatalogDAOImplJDBC();
        }
        else if(persistenceType == PersistenceType.FILE_SYSTEM){
            return new CatalogDAOImplFIle();
        }
        // se tutte e due le condizioni falliscono qualche cosa è andato storto;
        throw new CatalogDAOFactoryError("catalog DAO factory error!!");
    }
}
