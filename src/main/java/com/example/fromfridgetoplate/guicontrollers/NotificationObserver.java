package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import javafx.collections.ObservableList;

public interface NotificationObserver {


    void update(ObservableList<NotificationBean> nbl);
}