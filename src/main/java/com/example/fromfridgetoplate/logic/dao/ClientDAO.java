package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Client;

public interface ClientDAO {
    boolean saveClient(Client newClient) throws DAOException;
}
