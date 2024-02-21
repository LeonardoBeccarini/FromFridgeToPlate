package com.example.fromfridgetoplate.logic.model;

import com.example.fromfridgetoplate.patterns.decorator.Discountable;

public abstract class Coupon implements Discountable {
    private int code;
    private CouponType type;
    private double discount;

    // interfaccia sulla quale apllicare il decorator (il coupon)
    private Discountable disocuntable;

    protected Coupon(int code, CouponType type, double discount) {
        this.code = code;
        this.type = type;
        this.discount = discount;
    }

    public int getCode() {
        return code;
    }

    public Discountable getDisocuntable() {
        return disocuntable;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDisocuntable(Discountable disocuntable) {
        this.disocuntable = disocuntable;
    }


}
