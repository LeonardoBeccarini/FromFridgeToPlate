package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Shop;

import java.util.List;

public interface ShopDAO {

    boolean saveShop(Shop shop) throws  DAOException;

    Shop retrieveShopByEmail(String email) throws DAOException;

    List<Shop> retrieveShopByName(String name) throws DAOException;



}
