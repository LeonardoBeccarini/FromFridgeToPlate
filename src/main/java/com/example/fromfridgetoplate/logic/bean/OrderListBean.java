package com.example.fromfridgetoplate.logic.bean;

import com.example.fromfridgetoplate.logic.control.PendingOrdersController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderListBean {
    private List<OrderBean> orderBeans = new ArrayList<>(); // la inizializzo per non farmi restituire null, ma la lista vuota, quando la listaè vuota


    private boolean validateOrderComplete(OrderBean order) {
        if (order.getOrderId() == 0 || order.getCustomerId() == null || order.getShippingCity() == null) {
            return false;
        }

        return true;
    }

    private boolean validateOrderDate(OrderBean order) {
        LocalDateTime now = LocalDateTime.now();
        return !(order.getOrderTime().isAfter(now) );
    }

    public void validateAllOrders() {
        for (int i = orderBeans.size() - 1; i >= 0; i--) {
            OrderBean order = orderBeans.get(i);
            if (!validateOrderComplete(order) || !validateOrderDate(order)) {
                orderBeans.remove(i);  // Rimuove l'ordine non valido
            }
        }
    }




    public List<OrderBean> getOrderBeans() {
        return orderBeans;
    }


    public void setOrderBeans(List<OrderBean> orderBeans) {
        this.orderBeans = orderBeans;
    }






}