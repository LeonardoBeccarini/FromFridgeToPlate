package com.example.fromfridgetoplate.patterns.observer;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;

import java.util.ArrayList;
import java.util.List;


public abstract class NotificationList {

    List<NotificationObserver> ntfObservers = new ArrayList<>();
    protected List<NotificationBean> notifications;

    public void attach(NotificationObserver obs){
        ntfObservers.add(obs);
    }
    public void detach(NotificationObserver obs){
        ntfObservers.remove(obs);
    }

    public void notifyGUI (){
        for(NotificationObserver obs : ntfObservers){
            obs.update();
        }
    }


}
