package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.User;

import java.sql.*;

public class DbUserDAO implements UserDAO {
    public DbUserDAO(){/*costruttore*/}
    public User verifyUserCredentials(String email, String password)throws NotExistentUserException {
        Connection connection = SingletonConnector.getInstance().getConnection();
        int role = 0;
        try ( CallableStatement cs = connection.prepareCall("{call login(?,?,?)}")){
            cs.setString(1, email);
            cs.setString(2, password);
            cs.registerOutParameter(3, Types.NUMERIC);
            cs.executeQuery();
            role = cs.getInt(3);
            if(role == 4) {
                throw new NotExistentUserException("user not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User(email, Role.fromInt(role));
    }

}

