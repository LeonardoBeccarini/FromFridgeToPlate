package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.logic.control.PendingOrdersController;
import com.example.fromfridgetoplate.logic.dao.OrderDAO;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class OrderListBean {
    private List<OrderBean> orderBeans = new ArrayList<>(); // la inizializzo per non farmi restituire null, ma la lista vuota, quando la listaè vuota

    // Costruttore
    public OrderListBean() {
    }

    // Metodo getter per ottenere la lista di OrderBean
    public List<OrderBean> getOrderBeans() {
        return orderBeans;
    }

    // Metodo setter per impostare la lista di OrderBean
    public void setOrderBeans(List<OrderBean> orderBeans) {
        this.orderBeans = orderBeans;
    }


    public void refreshOrders() {
        PendingOrdersController poc = new PendingOrdersController();
        OrderListBean updatedOrderList = poc.getPendingOrderListBean();
        this.setOrderBeans(updatedOrderList.getOrderBeans());
    }


        // Eventuali altri metodi utili per la gestione della lista di ordini,
        // come metodi per aggiungere o rimuovere ordini, ecc.



}