package com.example.fromfridgetoplate.patterns.state;

import javafx.scene.control.Alert;

public class OfflineState implements RiderState {
    @Override
    public void goOnline(RiderStateContext context) {
        context.setState(new OnlineState());
        //   attivare la disponibilità del rider, iniziare il polling delle notifiche, ecc.
        context.getController().updateUIForOnlineState();
    }

    @Override
    public void goOffline(RiderStateContext context) {
        context.getController().showAlreadyOfflineAlert();
    }

}