package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Client;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.User;

import java.io.IOException;
import java.util.List;

public class FileClientDAO extends FileDAOBase implements ClientDAO {
    public boolean saveClient(Client newClient) throws DAOException {
        // Verifica se il cliente esiste già nel file degli utenti
        if (isUserExists(newClient.getEmail())) {
            System.out.println("Cliente già registrato con questa email.");
            return false;
        }

        // Aggiungi il cliente al file degli utenti
        User newUser = new User(newClient.getEmail(), newClient.getPassword(), Role.CLIENT);
        if (!addUserToFile(newUser)) {
            System.out.println("Non è stato possibile aggiungere l'utente al file degli utenti.");
            return false;
        }

        // Aggiungo il cliente al file dei clientss
        List<Client> clients = readClientsFromFile();
        clients.add(newClient);
        try {
            writeToFile(clients, clientsFilePath);
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file dei clienti: " + e.getMessage());
            return false;
        }

        return true;
    }

    private boolean isUserExists(String email) throws DAOException{
        List<User> users = readUsersFromFile();
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean addUserToFile(User user ) throws DAOException{
        List<User> users = readUsersFromFile();
        users.add(user);
        try {
            writeToFile(users, usersFilePath);
            return true;
        } catch (IOException e) {
            System.out.println("Errore nella scrittura del file: " + e.getMessage());
            return false;
        }
    }


    private List<Client> readClientsFromFile() throws DAOException {
        return readFromFile(clientsFilePath);
    }


}
