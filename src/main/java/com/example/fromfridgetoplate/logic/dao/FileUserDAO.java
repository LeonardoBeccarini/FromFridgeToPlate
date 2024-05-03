package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.User;
import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class FileUserDAO extends FileDAOBase implements UserDAO {

    public User verifyUserCredentials(String email, String password) throws NotExistentUserException, DAOException {
        System.out.println("check verifyUserCredentials");
        List<User> users = null;
        users = readUsersFromFile();

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                System.out.println("e: " + user.getEmail() + " pw: " + user.getPassword());
                return new User(email, user.getRole());
            }

        }

        if (users.isEmpty()) {
            System.out.println("Nessun utente trovato nel file.");
        } else {
            System.out.println("Utenti trovati nel file:");
            for (User user : users) {
                System.out.println("Email: " + user.getEmail() + ", Password: " + user.getPassword() + ", Ruolo: " + user.getRole());
            }
        }

        //return new User(email, Role.CLIENT ); // da elimianre

        throw new NotExistentUserException("User not found");
    }



    public static void main(String[] args) throws DAOException {

        FileUserDAO fileUserDAO = new FileUserDAO();
        // Test readUsersFromFile
        List<User> users = fileUserDAO.readUsersFromFile();
        if (users.isEmpty()) {
            System.out.println("Nessun utente trovato nel file.");
        } else {
            System.out.println("Utenti trovati nel file:");
            for (User user : users) {
                System.out.println("Email: " + user.getEmail() + ", Password: " + user.getPassword() + ", Ruolo: " + user.getRole());
            }
        }
    }



}







