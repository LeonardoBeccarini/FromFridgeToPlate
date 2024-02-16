package com.example.fromfridgetoplate.logic.bean;

public class CouponBean {
    private int code;
    private String vatNumber;
    private double discount;

    public CouponBean(int code, String vatNumber) {
        this.code = code;
        this.vatNumber = vatNumber;
    }

    public CouponBean(int code, String vatNumber, double discount) {
        this.code = code;
        this.vatNumber = vatNumber;
        this.discount = discount;
    }

    public CouponBean(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getVatNumber() {
        return vatNumber;
    }
}
