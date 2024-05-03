package com.example.fromfridgetoplate.patterns.state;

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