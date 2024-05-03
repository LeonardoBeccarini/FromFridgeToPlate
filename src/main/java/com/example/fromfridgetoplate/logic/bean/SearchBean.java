package com.example.fromfridgetoplate.logic.bean;

public class SearchBean {

    private String  shippingCity;

    private final OrderBean selectedOrderBean;



    public SearchBean(String city, OrderBean ordBean) {
        this.shippingCity = city;
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

}
