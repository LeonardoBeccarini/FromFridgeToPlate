package com.example.fromfridgetoplate.patterns.state;


public class OfflineState extends RiderState {
    @Override
    public void goOnline(RiderStateContext context) {
        context.setState(new OnlineState());

        context.getController().updateUIForOnlineState();
    }

    @Override
    public void goOffline(RiderStateContext context) {
        context.showAlreadyOffAlert();
    }

}