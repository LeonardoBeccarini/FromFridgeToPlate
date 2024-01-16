package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.model.Catalog;
import com.example.fromfridgetoplate.logic.model.FoodItem;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CatalogDAO {
    public Catalog retrieveCatalog(String vatNumber){
        Catalog catalog = new Catalog();
        Connection connection = SingletonConnector.getInstance().getConnection();
        try(CallableStatement cs = connection.prepareCall("{call retrieveCatalog(?)}")){
            cs.setString(1, vatNumber);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                FoodItem foodItem = new FoodItem(rs.getString(1), rs.getFloat(2));
                catalog.addIngredient(foodItem);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return catalog;
    }
    public void addItem(String name, float price, String shopName){
        Connection connection = SingletonConnector.getInstance().getConnection();
        try (CallableStatement cs = connection.prepareCall("{call addToCatalogo(?,?,?)}")) {
            cs.setString(1, name);
            cs.setFloat(2, price);
            cs.setString(3, shopName);
            cs.executeQuery();

        }catch (SQLException e) {
           e.printStackTrace();
        }
    }
}
