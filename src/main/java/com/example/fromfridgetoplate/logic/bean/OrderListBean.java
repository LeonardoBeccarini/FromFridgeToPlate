package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.logic.control.PendingOrdersController;

import java.util.ArrayList;
import java.util.List;

public class OrderListBean {
    private List<OrderBean> orderBeans = new ArrayList<>(); // la inizializzo per non farmi restituire null, ma la lista vuota, quando la listaè vuota

    public List<OrderBean> getOrderBeans() {
        return orderBeans;
    }


    public void setOrderBeans(List<OrderBean> orderBeans) {
        this.orderBeans = orderBeans;
    }






}