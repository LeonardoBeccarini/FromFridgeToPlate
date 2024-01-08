package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.model.Catalog;
import com.example.fromfridgetoplate.logic.model.FoodItem;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CatalogDAO {
    public Catalog retrieveCatalog(String shopName){
        Catalog catalog = new Catalog();
        Connection connection = SingletonConnector.getInstance().getConnection();
        try(CallableStatement cs = connection.prepareCall("{call retrieveCatalog(?)}")){
            cs.setString(1, shopName);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                catalog.addIngredient(new FoodItem(cs.getString(1), cs.getFloat(2)));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return catalog;
    }
}
