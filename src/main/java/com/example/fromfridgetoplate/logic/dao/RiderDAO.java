package com.example.fromfridgetoplate.logic.dao;


import com.example.fromfridgetoplate.logic.model.Rider;


public interface RiderDAO {

    void setRiderAvailable(int riderId, boolean isAval);
    Rider getRiderDetailsFromSession();
    boolean registerRider(Rider rider);

}
