package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.model.Shop;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopDAO {
    public boolean saveShop(String email, String password, String name, String VATnumber, String address, String phoneNumber){
        Connection connection = SingletonConnector.getInstance().getConnection();

        try(CallableStatement cs = connection.prepareCall("{call registerShop(?,?,?,?,?,?)}")){
            cs.setString(1,email);
            cs.setString(2,password);
            cs.setString(3,name);
            cs.setString(4,VATnumber);
            cs.setString(5,address);
            cs.setString(6, phoneNumber);
            cs.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true; /*potrei farlo meglio, non sono neanche sicurissimo sia corretto far tornare un booleano*/
    }
    public Shop retrieveShopByEmail(String email){
        Connection connection = SingletonConnector.getInstance().getConnection();
        Shop shop = null;
        try(CallableStatement cs = connection.prepareCall("{call retrieveShopByEmail(?)}")){
            cs.setString(1, email);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                 shop = new Shop(rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(2), rs.getString(5) );
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return shop;
    }
}
