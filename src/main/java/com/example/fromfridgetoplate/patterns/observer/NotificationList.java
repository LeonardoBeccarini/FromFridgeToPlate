package com.example.fromfridgetoplate.patterns.observer;

import com.example.fromfridgetoplate.guicontrollers.NotificationObserver;
import java.util.ArrayList;
import java.util.List;


public abstract class NotificationList {

    private List<NotificationObserver> ntfObservers = new ArrayList<>();

    public void attach(NotificationObserver obs){
        ntfObservers.add(obs);  // controllerJavaFx o controllerCLI, questi 2 controller grafici implementano la stessa interfaccia NotificationObserver
    }
    public void detach(NotificationObserver obs){
        ntfObservers.remove(obs);
    }

    protected void notifyObs (){ // protected perchè voglio che solo il possessore dello stato, ovvero CachingNotificaionList possa inviare le notifiche
        // se avviene un cambiamento di stato
        for(NotificationObserver obs : ntfObservers){
            obs.update();
        }
    }


}
