package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Catalog;

import java.io.IOException;

public interface CatalogDAO {
    Catalog retrieveCatalog(String vatNumber)throws DAOException, IOException;
    void addItem(String name, float price, String shopName)throws DAOException, IOException;
}
