package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;

import java.util.List;

public interface NotificationObserver {


    void update();

    void update(List<NotificationBean> ntfBeans); // overload di update(), per inviare notifiche specifiche agli observer che necessitano di questa forma di dati.
}

