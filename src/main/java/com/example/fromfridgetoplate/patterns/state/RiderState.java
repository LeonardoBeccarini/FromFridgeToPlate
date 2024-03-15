package com.example.fromfridgetoplate.patterns.state;

public interface RiderState {
    void goOnline(RiderStateContext context);
    void goOffline(RiderStateContext context);
    //void viewNotifications(RiderStateContext context);
    //void deliverOrder(RiderStateContext context, OrderBean order);
}

