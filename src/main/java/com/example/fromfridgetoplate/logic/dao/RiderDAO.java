package com.example.fromfridgetoplate.logic.dao;


import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Rider;


public interface RiderDAO {

    void setRiderAvailable(int riderId, boolean isAval) throws DAOException;
    Rider getRiderDetailsFromSession() throws DAOException;
    boolean registerRider(Rider rider) throws DAOException;

}
