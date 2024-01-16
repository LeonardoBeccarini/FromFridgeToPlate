package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.model.Notification;

import java.util.List;


public class RiderNotificationHandler implements OrdersObserver {
    private final RiderHPController riderController;
    private final RiderBean rider;


    public RiderNotificationHandler(RiderHPController riderController, RiderBean riderBean) {
        this.riderController = riderController;
        this.rider = riderBean;
    }

    @Override
    public void update(OrderBean orderBean, List<Notification> notifications) {
        System.out.println("riderNotificationHandler" );
        riderController.update(orderBean);
    }
}



