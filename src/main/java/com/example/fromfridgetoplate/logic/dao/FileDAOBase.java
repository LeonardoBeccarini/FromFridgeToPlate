package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.logic.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class FileDAOBase {
    protected Properties properties = new Properties();

    protected String ordersFilePath;
    protected String clientsFilePath;
    protected String ridersFilePath;

    protected String assignedOrdersFilePath;
    protected String shopsFilePath;

    protected String usersFilePath;
    protected String catalogFilePath;

    protected FileDAOBase() {
        loadProperties();
        this.ordersFilePath = properties.getProperty("ordersFilePath");
        this.ridersFilePath = properties.getProperty("ridersFilePath");
        this.assignedOrdersFilePath = properties.getProperty("assignedOrdersFilePath");
        this.shopsFilePath = properties.getProperty("shopsFilePath");
        this.usersFilePath = properties.getProperty("usersFilePath");
        this.catalogFilePath = properties.getProperty("catalogFilePath");
        this.clientsFilePath = properties.getProperty("clientsFilePath");
    }

    private void loadProperties() {
        String relativePath = "src/main/resources/com/example/Properties/files_config.properties";
        try (InputStream input = new FileInputStream(relativePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected <T> List<T> readFromFile(String filePath) throws DAOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) { // Controlla anche se il file è vuoto
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List) {
                return (List<T>) readObject;
            } else {
                System.err.println("Tipo Dati scritti male nel file: non è una lista.");
                return new ArrayList<>();
            }
        } catch (EOFException e) {


            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new DAOException("Errore di lettura da file: ", e.getCause());
        }
    }


    protected <T> void writeToFile(List<T> genericList, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(genericList);
        } catch (IOException e) {
            // Propago l'eccezione
            throw e;
        }
    }



    protected List<Order> getAllOrders() throws DAOException {

        // Utilizzo il metodo readFromFile ereditato dalla superclasse
        return this.readFromFile(ordersFilePath);
    }

    protected void writeOrdersToFile(List<Order> orders) {
        try {
            // Chiamo il metodo della superclasse per scrivere gli ordini sul file
            this.writeToFile(orders, this.ordersFilePath);
        } catch (IOException e) {
            // Scrivo l'errore su stderr
            System.err.println("Errore nella scrittura del file degli ordini: " + e.getMessage());

            // potrei anche fare:throw new RuntimeException("Errore nella scrittura del file degli ordini", e);
        }
    }




    protected List<Order> getAllAssignedOrders() throws DAOException {

        List<Order> assignedOrders = readFromFile(assignedOrdersFilePath);
        return  assignedOrders;

    }

    protected void writeAssignedOrdersToFile(List<Order> assignedOrders) throws IOException {
        // Uso il metodo writeToFile della superclasse
        writeToFile(assignedOrders, assignedOrdersFilePath);

    }


    // metodo per deserializzare la lista di riders
    protected List<Rider> getAllRiders() throws DAOException {

        List<Rider> riders = readFromFile(ridersFilePath);

        // Stampa i dettagli di ogni rider
        /*System.out.println("Rider estratti dal file:");
        for (Rider rider : riders) {
            System.out.println("ID: " + rider.getId() + ", Nome: " + rider.getName() + ", Cognome: " + rider.getSurname() +
                    ", Disponibilità: " + (rider.isAvailable() ? "Disponibile" : "Non disponibile") +
                    ", Città assegnata: " + rider.getAssignedCity());
        }*/

        return riders;
    }

    protected List<User> readUsersFromFile() throws DAOException {
        // Usa il metodo readFromFile della superclasse, fornendo il percorso del file e il tipo atteso
        List<User> users = readFromFile(usersFilePath);
        // Verifico che tipo degli elementi sia corretto
        if (!users.isEmpty() && users.get(0) != null) {
            System.out.println("username:"+ users.get(0).getEmail());
            return users;
        } else {
            return new ArrayList<>();
        }
    }

    // Serializza gli utenti registrati nel file
    protected boolean writeUsersToFile(List<User> users) {
        try {
            writeToFile(users, usersFilePath);
            return true; //  true se la scrittura è andata a buon fine
        } catch (IOException e) {
            System.err.println("Errore nella scrittura del file degli utenti: " + e.getMessage());
            return false;
        }
    }


}
