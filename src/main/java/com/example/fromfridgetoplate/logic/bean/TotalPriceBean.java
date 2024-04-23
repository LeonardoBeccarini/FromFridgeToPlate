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
        return (double) Math.round(totalPrice*100)/100; // in modo da restituire all'utente un valore arrotondato
    }

    public List<CouponBean> getCouponBeanList() {
        return couponBeanList;
    }

}
