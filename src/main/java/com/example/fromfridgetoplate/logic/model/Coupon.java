package com.example.fromfridgetoplate.logic.model;

import com.example.fromfridgetoplate.patterns.decorator.Discountable;

public abstract class Coupon implements Discountable {
    private int code;

    private double discount;

    // interfaccia sulla quale apllicare il concrete decorator (il coupon)
    private Discountable disocuntable;

    protected Coupon(int code, double discount) {
        this.code = code;
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
