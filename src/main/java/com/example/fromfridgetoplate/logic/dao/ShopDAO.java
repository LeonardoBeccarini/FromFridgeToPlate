package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Shop;

import java.util.List;

public interface ShopDAO {

    public boolean saveShop(Shop shop) throws DbException;

    public Shop retrieveShopByEmail(String email) throws DbException;

    public List<Shop> retrieveShopByName(String name) throws DbException;



}
