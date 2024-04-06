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

public class DbShopDAO implements ShopDAO {
    private static final  String ERROREDATABASE = "errore database";
    private Connection connection;
    public DbShopDAO(Connection conn) {this.connection = conn;}
    public DbShopDAO(){} // questo dovresti eliminarlo quando hai adattato il resto del tuo codice (becca) a usare la DAOfactory
    public boolean saveShop(Shop shop) throws DbException {

        try(CallableStatement cs = connection.prepareCall("{call registerShop(?,?,?,?,?,?)}")){
            cs.setString(1,shop.getEmail());
            cs.setString(2,shop.getPassword());
            cs.setString(3,shop.getName());
            cs.setString(4,shop.getVATnumber());
            cs.setString(5,shop.getAddress());
            cs.setString(6, shop.getPhoneNumber());
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
