package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.guicontrollers.RiderSelectionListener;

public class SearchBean {

    private String  shippingCity;
    private RiderSelectionListener riderSelListener;

    private OrderBean selectedOrderBean;

    public SearchBean(String shippingCity, RiderSelectionListener riderSelListener, OrderBean selectedOrderBean) {
        this.shippingCity = shippingCity;
        this.riderSelListener = riderSelListener;
        this.selectedOrderBean = selectedOrderBean;
    }

    public String getCity() {
        return shippingCity;
    }

    public void setCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public RiderSelectionListener getRiderSelListener() {
        return riderSelListener;
    }

    public void setRiderSelListener(RiderSelectionListener riderSelListener) {
        this.riderSelListener = riderSelListener;
    }

    public OrderBean getSelectedOrderBean() {
        return selectedOrderBean;
    }

    public void setSelectedOrderBean(OrderBean selectedOrderBean) {
        this.selectedOrderBean = selectedOrderBean;
    }
}
