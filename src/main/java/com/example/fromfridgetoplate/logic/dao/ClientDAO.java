package com.example.fromfridgetoplate.logic.dao;


import com.example.fromfridgetoplate.logic.model.Client;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ClientDAO extends FileDAOBase {

    private String clientsFilePath;

    public ClientDAO() {
        super();
        this.clientsFilePath = properties.getProperty("clientsFilePath"); // Assicurati che questa chiave sia presente nel tuo file di proprietà
    }

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


    public boolean FilesaveClient(Client newClient) {
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

        // Aggiungi il cliente al file dei clienti
        List<Client> clients = readClientsFromFile();
        clients.add(newClient);
        writeToFile(clients, clientsFilePath);

        return true;
    }

    private boolean isUserExists(String email) {
        List<User> users = readUsersFromFile();
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean addUserToFile(User user) {
        List<User> users = readUsersFromFile();
        users.add(user);
        writeToFile(users, usersFilePath);
        return true;
    }

    private List<Client> readClientsFromFile() {
        return readFromFile(clientsFilePath);
    }

    public static void main(String[] args) {
        ClientDAO clientDAO = new ClientDAO();

        // Crea un nuovo oggetto Client per il test
        Client newClient = new Client("test4@test.com", "password123", "Nome", "Cognome", "Indirizzo 123");


        // Prova a salvare il cliente nel file
        boolean saveResult = clientDAO.FilesaveClient(newClient);
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



