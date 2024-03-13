package com.example.fromfridgetoplate.logic.bean;

import java.util.List;

public class TotalPriceBean {
    private Double totalPrice;
    private List<CouponBean> couponBeanList ;


    public TotalPriceBean(Double totalPrice, List<CouponBean> couponBeanList) {
        this.totalPrice = totalPrice;
        this.couponBeanList = couponBeanList;
    }

    public TotalPriceBean(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public List<CouponBean> getCouponBeanList() {
        return couponBeanList;
    }
}
