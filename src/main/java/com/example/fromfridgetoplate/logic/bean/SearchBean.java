package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.guicontrollers.IRiderSelectionListener;

public class SearchBean {

    private String  shippingCity;
    private IRiderSelectionListener riderSelListener;

    private OrderBean selectedOrderBean;

    public SearchBean(String shippingCity, IRiderSelectionListener riderSelListener, OrderBean selectedOrderBean) {
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

    public IRiderSelectionListener getRiderSelListener() {
        return riderSelListener;
    }

    public void setRiderSelListener(IRiderSelectionListener riderSelListener) {
        this.riderSelListener = riderSelListener;
    }

    public OrderBean getSelectedOrderBean() {
        return selectedOrderBean;
    }

    public void setSelectedOrderBean(OrderBean selectedOrderBean) {
        this.selectedOrderBean = selectedOrderBean;
    }
}
