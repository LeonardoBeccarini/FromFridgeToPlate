package com.example.fromfridgetoplate.patterns.state;


import javafx.scene.control.Alert;

public class OnlineState implements RiderState {
    @Override
    public void goOnline(RiderStateContext context) {
        context.getController().showAlreadyOnlineAlert();
    }

    @Override
    public void goOffline(RiderStateContext context) {
        context.setState(new OfflineState());
        // disattivare la disponibilità del rider, stoppare polling delle notifiche, ecc.
        context.getController().updateUIForOfflineState();
    }


}