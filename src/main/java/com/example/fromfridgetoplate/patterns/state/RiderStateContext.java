package com.example.fromfridgetoplate.patterns.state;

import com.example.fromfridgetoplate.guicontrollers.RiderHomePageGraphicController;

public class RiderStateContext {
    private RiderState currentState;
    private RiderHomePageGraphicController controller;

    public RiderStateContext(RiderHomePageGraphicController controller) {
        this.controller = controller;
        this.currentState = new OfflineState(); // Stato iniziale
    }

    public void setState(RiderState state) {
        this.currentState = state;
    }

    public void goOnline() {
        currentState.goOnline(this);
    }

    public void goOffline() {
        currentState.goOffline(this);
    }

    

    public RiderHomePageGraphicController getController() {
        return controller;
    }
}