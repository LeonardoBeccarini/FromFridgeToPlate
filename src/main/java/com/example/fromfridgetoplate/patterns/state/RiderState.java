package com.example.fromfridgetoplate.patterns.state;

public abstract class RiderState {
    abstract void goOnline(RiderStateContext context);

    abstract void goOffline(RiderStateContext context);
}

