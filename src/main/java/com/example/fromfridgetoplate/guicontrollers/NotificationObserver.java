package com.example.fromfridgetoplate.guicontrollers;

import com.example.fromfridgetoplate.logic.bean.NotificationBean;


import java.util.List;

public interface NotificationObserver {


    void update(List<NotificationBean> nbl);
}