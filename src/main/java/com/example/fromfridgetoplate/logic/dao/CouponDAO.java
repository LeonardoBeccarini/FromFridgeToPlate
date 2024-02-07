package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.CouponNotFoundException;
import com.example.fromfridgetoplate.logic.model.Coupon;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponDAO {
    public Coupon retrieveCoupon(String vatNumber, int code) throws CouponNotFoundException{
        Connection connection = SingletonConnector.getInstance().getConnection();
        Coupon retrievedCoupon = null;
        try(CallableStatement cs = connection.prepareCall("{call retrieveCoupon(?, ?)}")){
            cs.setString(1, vatNumber);
            cs.setInt(2, code);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            while(rs.next()){
                retrievedCoupon = new Coupon(rs.getInt(1));
            }
        }catch(SQLException e){
           throw new CouponNotFoundException("coupon not found");
        }
        return retrievedCoupon;
    }
}
