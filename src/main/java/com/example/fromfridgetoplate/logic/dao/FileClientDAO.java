package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.model.Client;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.User;

import java.io.IOException;
import java.util.List;

public class FileClientDAO extends FileDAOBase implements ClientDAO {
    public boolean saveClient(Client newClient) {
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

    private boolean isUserExists(String email) {
        List<User> users = readUsersFromFile();
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean addUserToFile(User user) {
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


    private List<Client> readClientsFromFile() {
        return readFromFile(clientsFilePath);
    }

    public static void main(String[] args) {
        FileClientDAO clientDAO = new FileClientDAO();

        // Crea un nuovo oggetto Client per il test
        Client newClient = new Client("test4@test.com", "password123", "Nome", "Cognome", "Indirizzo 123");


        // Prova a salvare il cliente nel file
        boolean saveResult = clientDAO.saveClient(newClient);
        System.out.println("Risultato del salvataggio: " + (saveResult ? "Successo" : "Fallimento"));

        // Leggi i clienti dal file e stampali per verificare l'inserimento
        List<Client> clients = clientDAO.readClientsFromFile();
        System.out.println("Clienti registrati nel file:");
        for (Client client : clients) {
            System.out.println("Email: " + client.getEmail() +
                    ", Nome: " + client.getName() +
                    ", Cognome: " + client.getSurname() +
                    ", Indirizzo: " + client.getAddress());
        }
    }
}
