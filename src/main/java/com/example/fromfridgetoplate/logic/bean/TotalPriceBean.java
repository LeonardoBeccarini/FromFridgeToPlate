package com.example.fromfridgetoplate.logic.bean;

import java.util.List;

public class TotalPriceBean {
    private final Double totalPrice;
    private List<CouponBean> couponBeanList ;


    public TotalPriceBean(Double totalPrice, List<CouponBean> couponBeanList) {
        this(totalPrice);
        this.couponBeanList = couponBeanList;
    }

    public TotalPriceBean(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPrice() {
        return (double) Math.round(totalPrice*100) /100;
    }

    public List<CouponBean> getCouponBeanList() {
        return couponBeanList;
    }

}
