package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.model.Rider;

import java.util.List;

public interface RiderDAO {

    void setRiderAvailable(RiderBean riderBn);
    RiderBean getRiderDetailsFromSession();
    boolean registerRider(Rider rider);
}
