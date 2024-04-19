package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.guicontrollers.IRiderSelectionListener;

public class SearchBean {

    private String  shippingCity;

    private OrderBean selectedOrderBean;



    public SearchBean(String città, OrderBean ordBean) {
        this.shippingCity = città;
        this.selectedOrderBean = ordBean;
    }

    public String getCity() {
        return shippingCity;
    }

    public void setCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }


    public OrderBean getSelectedOrderBean() {
        return selectedOrderBean;
    }

    public void setSelectedOrderBean(OrderBean selectedOrderBean) {
        this.selectedOrderBean = selectedOrderBean;
    }
}
