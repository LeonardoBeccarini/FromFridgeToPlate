package com.example.fromfridgetoplate.logic.model;

import com.example.fromfridgetoplate.logic.exceptions.NegativePriceException;
import com.example.fromfridgetoplate.patterns.decorator.Discountable;


public class CouponApplier {
    Discountable originalPrice;
    Discountable finalPrice;
    CouponList couponList;

    public CouponApplier(Discountable originalPrice){
        this.originalPrice = originalPrice;
        couponList = new CouponList();
        finalPrice = originalPrice;

    }
    public void applyCoupon(Coupon coupon) throws NegativePriceException {
        if(couponList.verifyPresence(coupon) == null){
            couponList.addCoupon(coupon);
        }
        try {
            applyDiscount();
        }catch (NegativePriceException negativePriceException){
            couponList.remove(coupon);
            applyDiscount();
            throw negativePriceException;
        }
    }

    private void applyDiscount() throws NegativePriceException {
        finalPrice = originalPrice;
        for(Coupon coupon: couponList.getCouponList()){
            if(coupon != null){
                coupon.setDisocuntable(finalPrice);
                if(coupon.getPrice()<0) {
                    throw new NegativePriceException("current coupon makes the price negative!");
                }
                finalPrice = coupon;
            }
        }
    }

    public CouponList getCouponList() {
        return couponList;
    }

    public Discountable getFinalPrice() {
        return finalPrice;
    }
}


