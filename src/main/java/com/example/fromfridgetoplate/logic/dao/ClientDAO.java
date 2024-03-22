package com.example.fromfridgetoplate.logic.dao;


import com.example.fromfridgetoplate.logic.model.Client;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class ClientDAO {
    public boolean saveClient(Client newClient){
        Connection connection = SingletonConnector.getInstance().getConnection();

        try(CallableStatement cs = connection.prepareCall("{call registerClient(?,?,?,?,?)}")){
            cs.setString(1, newClient.getEmail());
            cs.setString(2, newClient.getPassword());
            cs.setString(3, newClient.getName());
            cs.setString(4, newClient.getSurname());
            cs.setString(5, newClient.getAddress());
            cs.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return true; /*potrei farlo meglio, non sono neanche sicurissimo sia corretto far tornare un booleano*/
    }
}
