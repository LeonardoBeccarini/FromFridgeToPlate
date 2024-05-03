package com.example.fromfridgetoplate.patterns.observer;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import com.example.fromfridgetoplate.logic.bean.NotificationBean;
import com.example.fromfridgetoplate.logic.model.Notification;

import java.util.ArrayList;
import java.util.List;


public abstract class NotificationList {

    protected List<NotificationObserver> ntfObservers = new ArrayList<>();

    //protected List<NotificationBean> notifications;




    public void attach(NotificationObserver obs){
        ntfObservers.add(obs);  // controllerJavaFx o controllerCLI, questi 2 controller grafici implementano la stessa interfaccia NotificationObserver
    }
    public void detach(NotificationObserver obs){
        ntfObservers.remove(obs);
    }

    public void notifyObs (){
        for(NotificationObserver obs : ntfObservers){
            obs.update();
        }
    }


}
