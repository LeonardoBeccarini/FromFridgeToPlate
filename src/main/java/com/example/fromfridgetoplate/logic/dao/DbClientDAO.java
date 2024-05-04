package com.example.fromfridgetoplate.logic.dao;


import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Client;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DbClientDAO implements ClientDAO {

    public boolean saveClient(Client newClient) throws DAOException {
        Connection connection = SingletonConnector.getInstance().getConnection();

        try(CallableStatement cs = connection.prepareCall("{call registerClient(?,?,?,?,?)}")){
            cs.setString(1, newClient.getEmail());
            cs.setString(2, newClient.getPassword());
            cs.setString(3, newClient.getName());
            cs.setString(4, newClient.getSurname());
            cs.setString(5, newClient.getAddress());
            cs.executeQuery();
        }catch(SQLException e){
            throw new DAOException("errore database: " + e.getMessage());
        }
        return true;
    }
}



