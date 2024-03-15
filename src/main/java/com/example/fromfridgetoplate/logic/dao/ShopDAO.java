package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Shop;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShopDAO {
    private static final  String ERROREDATABASE = "errore database";
    private Connection connection;
    public ShopDAO(Connection conn) {this.connection = conn;}
    public ShopDAO(){} // questo dovresti eliminarlo quando hai adattato il resto del tuo codice (becca) ad usare la DAOfactory
    public boolean saveShop(String email, String password, String name, String vatNumber, String address, String phoneNumber) throws DbException {

        try(CallableStatement cs = connection.prepareCall("{call registerShop(?,?,?,?,?,?)}")){
            cs.setString(1,email);
            cs.setString(2,password);
            cs.setString(3,name);
            cs.setString(4,vatNumber);
            cs.setString(5,address);
            cs.setString(6, phoneNumber);
            cs.executeQuery();
        }catch(SQLException e){
            throw new DbException(ERROREDATABASE+ e.getMessage());
        }
        return true; /*potrei farlo meglio, non sono neanche sicurissimo sia corretto far tornare un booleano*/
    }
    public Shop retrieveShopByEmail(String email) throws DbException {
        Connection conn = SingletonConnector.getInstance().getConnection();
        Shop shop = null;
        try(CallableStatement cs = conn.prepareCall("{call retrieveShopByEmail(?)}")){
            cs.setString(1, email);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                 shop = new Shop(rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(2), rs.getString(5) );
            }
        }catch(SQLException e){
            throw new DbException(ERROREDATABASE+ e.getMessage());
        }
        return shop;
    }
    public List<Shop> retrieveShopByName(String name) throws DbException {
        Connection connect = SingletonConnector.getInstance().getConnection();
        List<Shop> shopList = new ArrayList<>();
        try(CallableStatement cs = connect.prepareCall("{call retrieveShopByName(?)}")){
            cs.setString(1, name);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                Shop shop = new Shop(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                Objects.requireNonNull(shopList).add(shop);
            }
        }catch(SQLException e){
            throw new DbException(ERROREDATABASE+ e.getMessage());
        }
        return shopList;
    }
}
