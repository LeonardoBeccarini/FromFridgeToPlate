package com.example.fromfridgetoplate.logic.dao;

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
    protected String ridersFilePath;

    protected String assignedOrdersFilePath;
    protected String shopsFilePath;

    protected String usersFilePath;

    public FileDAOBase() {
        loadProperties();
        this.ordersFilePath = properties.getProperty("ordersFilePath");
        this.ridersFilePath = properties.getProperty("ridersFilePath");
        this.assignedOrdersFilePath = properties.getProperty("assignedOrdersFilePath");
        this.shopsFilePath = properties.getProperty("shopsFilePath");
        this.usersFilePath = properties.getProperty("usersFilePath");
    }

    private void loadProperties() {
        String relativePath = "src/main/resources/com/example/Properties/files_config.properties";
        try (InputStream input = new FileInputStream(relativePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected <T> List<T> readFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    protected <T> void writeToFile(List<T> genericList, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(genericList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    protected List<Order> getAllOrders() {

        // Utilizzo il metodo readFromFile ereditato dalla superclasse
        return this.readFromFile(ordersFilePath);
    }

    protected void writeOrdersToFile(List<Order> orders) {

        // Chiamo il metodo della superclasse per scrivere gli ordini sul file
        this.writeToFile(orders, this.ordersFilePath);
    }


    protected List<Order> getAllAssignedOrders() {

        List<Order> assignedOrders = readFromFile(assignedOrdersFilePath);
        return  assignedOrders;

    }

    protected void writeAssignedOrdersToFile(List<Order> assignedOrders) throws IOException {
        // Uso il metodo writeToFile della superclasse
        writeToFile(assignedOrders, assignedOrdersFilePath);

    }


    // metodo per deserializzare la lista di riders
    protected List<Rider> getAllRiders() {

        List<Rider> riders = readFromFile(ridersFilePath);

        // Stampa i dettagli di ogni rider
        System.out.println("Rider estratti dal file:");
        for (Rider rider : riders) {
            System.out.println("ID: " + rider.getId() + ", Nome: " + rider.getName() + ", Cognome: " + rider.getSurname() +
                    ", Disponibilità: " + (rider.isAvailable() ? "Disponibile" : "Non disponibile") +
                    ", Città assegnata: " + rider.getAssignedCity());
        }

        return riders;
    }

    protected List<User> readUsersFromFile() {
        // Usa il metodo readFromFile della superclasse, fornendo il percorso del file e il tipo atteso
        List<User> users = readFromFile(usersFilePath);
        // Verifico che  tipo degli elementi sia corretto
        if (!users.isEmpty() && users.get(0) != null) {
            return users;
        } else {
            return new ArrayList<>();
        }
    }

    // Serializza gli utenti registrati nel file
    protected boolean writeUsersToFile(List<User> users) {

        writeToFile(users, usersFilePath);
        return true;
    }












}
