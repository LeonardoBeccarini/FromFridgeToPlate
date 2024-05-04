package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Client;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.User;

import java.io.IOException;
import java.util.List;

public class FileClientDAO extends FileDAOBase implements ClientDAO {
    public boolean saveClient(Client newClient) throws DAOException {
        boolean outcome = true;
        // Verifica se il cliente esiste già nel file degli utenti
        if (isUserExists(newClient.getEmail())) {
            throw new DAOException("Cliente già registrato con questa email.");
        }

        // Aggiungi il cliente al file degli utenti
        User newUser = new User(newClient.getEmail(), newClient.getPassword(), Role.CLIENT);
        try{
            addUserToFile(newUser);
        }catch (DAOException e){
            throw new DAOException(e.getMessage());
        }
        // Aggiungo il cliente al file dei clientss
        List<Client> clients = readClientsFromFile();
        clients.add(newClient);
        try {
            writeToFile(clients, clientsFilePath);
        } catch (IOException e) {
            throw new DAOException("Exception FileDAO: " + e.getMessage());
        }
        return outcome;
    }

    private boolean isUserExists(String email) throws DAOException{
        List<User> users = readUsersFromFile();
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private void addUserToFile(User user ) throws DAOException{
        List<User> users = readUsersFromFile();
        users.add(user);
        try {
            writeToFile(users, usersFilePath);
        } catch (IOException e) {
            throw new DAOException("Errore nella scrittura del file: " + e.getMessage());
        }
    }


    private List<Client> readClientsFromFile() throws DAOException {
        return readFromFile(clientsFilePath);
    }


}
