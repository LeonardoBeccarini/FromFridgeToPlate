package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Shop;

import java.util.List;

public interface ShopDAO {

    boolean saveShop(Shop shop) throws DbException, DAOException;

    Shop retrieveShopByEmail(String email) throws DbException;

    List<Shop> retrieveShopByName(String name) throws DbException;



}
