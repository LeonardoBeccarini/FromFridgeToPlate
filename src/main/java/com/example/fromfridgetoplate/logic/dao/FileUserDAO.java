package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.User;
import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;

import java.util.List;

public class FileUserDAO extends FileDAOBase implements UserDAO {

    public User verifyUserCredentials(String email, String password) throws NotExistentUserException, DAOException {
        List<User> users = null;
        users = readUsersFromFile();

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {

                return new User(email, user.getRole());
            }

        }

        //return new User(email, Role.CLIENT ); // da elimianre

        throw new NotExistentUserException("User not found");
    }



}







