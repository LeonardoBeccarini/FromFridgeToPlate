package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;
import com.example.fromfridgetoplate.logic.model.User;

public interface UserDAO {

    public User verifyUserCredentials(String email, String password)throws NotExistentUserException;
}
