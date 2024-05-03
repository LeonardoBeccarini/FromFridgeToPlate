package com.example.fromfridgetoplate.logic.model;

public class SubtractionCoupon extends Coupon{
    public SubtractionCoupon(int code, double discount) {
        super(code,discount);

    }
    // concrete decorator
    public Double getPrice(){
        return getDisocuntable().getPrice() - getDiscount();
    }
}
