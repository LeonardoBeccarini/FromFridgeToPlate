package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.CouponNotFoundException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Coupon;
import com.example.fromfridgetoplate.logic.model.CouponType;
import com.example.fromfridgetoplate.logic.model.SubtractionCoupon;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CouponDAO {
    public Coupon retrieveCoupon(String vatNumber, int code) throws CouponNotFoundException, DbException{
        Connection connection = SingletonConnector.getInstance().getConnection();
        Coupon retrievedCoupon = null;
        try(CallableStatement cs = connection.prepareCall("{call retrieveCoupon(?, ?)}")){
            cs.setString(1, vatNumber);
            cs.setInt(2, code);
            cs.execute();
            ResultSet rs = cs.getResultSet();

            while(rs.next()) {
                if(CouponType.fromInt(rs.getInt(3)) == CouponType.SUBTRACTION)
                    retrievedCoupon = new SubtractionCoupon(rs.getInt(1), rs.getDouble(2));
            }

           if(retrievedCoupon == null) throw new CouponNotFoundException("coupon not found!!");

        }catch(SQLException e){
            throw new DbException("errore database" + e.getMessage());
        }
        return retrievedCoupon;
    }
    public void deleteCoupon(String vatNumber, int code) throws DbException {
        Connection connection = SingletonConnector.getInstance().getConnection();
        try(CallableStatement cs = connection.prepareCall("{call deleteCoupon(?, ?)}")) {
            cs.setString(1, vatNumber);
            cs.setInt(2, code);
            cs.execute();
        }catch (SQLException e){
            throw new DbException("errore database" + e.getMessage());
        }
    }
}
