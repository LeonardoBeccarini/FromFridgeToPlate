package com.example.fromfridgetoplate.logic.model;

import java.util.ArrayList;
import java.util.List;

public class CouponList {
    private List<Coupon> coupons;

    public CouponList(){
        coupons = new ArrayList<>();
    }
    public void addCoupon(Coupon coupon){
        coupons.add(coupon);
    }
    public Coupon verifyPresence(Coupon coupon){
        for (Coupon coupon1: coupons){
            if(coupon1.getCode() == coupon.getCode()) {
                return coupon;
            }
        }
        return null;
    }
    public void remove(Coupon coupon){
        coupons.remove(coupon);
    }
    public List<Coupon> getCouponList(){
        return coupons;
    }
}
