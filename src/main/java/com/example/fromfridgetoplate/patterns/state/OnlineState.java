package com.example.fromfridgetoplate.patterns.state;

public class OnlineState extends RiderState {
    @Override
    public void goOnline(RiderStateContext context) {
        context.showAlreadyOnlAlert();
    }

    @Override
    public void goOffline(RiderStateContext context) {
        context.setState(new OfflineState());

        context.getController().updateUIForOfflineState();
    }


}