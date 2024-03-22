package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Catalog;

import java.io.IOException;

public interface CatalogDAO {
    Catalog retrieveCatalog(String vatNumber)throws DbException, IOException;
    void addItem(String name, float price, String shopName)throws DbException, IOException;
}
