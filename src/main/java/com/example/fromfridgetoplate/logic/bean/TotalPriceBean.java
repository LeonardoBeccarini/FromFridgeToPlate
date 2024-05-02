package com.example.fromfridgetoplate.logic.bean;

import java.text.DecimalFormat;
import java.util.List;

public class TotalPriceBean {
    private final Double totalPrice;
    private List<CouponBean> couponBeanList ;


    public TotalPriceBean(Double totalPrice, List<CouponBean> couponBeanList) {
        this.totalPrice = totalPrice;
        this.couponBeanList = couponBeanList;
    }

    public TotalPriceBean(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPrice() {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(totalPrice));
    }

    public List<CouponBean> getCouponBeanList() {
        return couponBeanList;
    }

}
