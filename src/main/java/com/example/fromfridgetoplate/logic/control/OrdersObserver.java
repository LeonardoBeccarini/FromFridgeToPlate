package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.model.Notification;
import com.example.fromfridgetoplate.logic.model.Order;

import java.util.List;

public interface OrdersObserver {
    void update(OrderBean orderBean, List<Notification> notifications);
}
