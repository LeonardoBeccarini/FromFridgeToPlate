package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
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

    protected FileDAOBase() throws ConfigurationException {
        loadProperties();
        this.ordersFilePath = properties.getProperty("ordersFilePath");
        this.ridersFilePath = properties.getProperty("ridersFilePath");
        this.assignedOrdersFilePath = properties.getProperty("assignedOrdersFilePath");
        this.shopsFilePath = properties.getProperty("shopsFilePath");
        this.usersFilePath = properties.getProperty("usersFilePath");
        this.catalogFilePath = properties.getProperty("catalogFilePath");
        this.clientsFilePath = properties.getProperty("clientsFilePath");
    }

    private void loadProperties() throws ConfigurationException {
        String relativePath = "src/main/resources/com/example/Properties/files_config.properties";
        try (InputStream input = new FileInputStream(relativePath)) {
            properties.load(input);
        } catch (IOException ex) {
            throw new ConfigurationException("Impossibile caricare il file di configurazione: " + relativePath, ex);
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
        }
    }



    protected List<Order> getAllOrders() throws DAOException {

        // Utilizzo il metodo readFromFile ereditato dalla superclasse
        return this.readFromFile(ordersFilePath);
    }

    protected void writeOrdersToFile(List<Order> orders) throws DAOException {
        try {
            // Chiamo il metodo della superclasse per scrivere gli ordini sul file
            this.writeToFile(orders, this.ordersFilePath);
        } catch (IOException e) {
            // Rilancio l'eccezione come DAOException
            throw new DAOException("Errore nella scrittura del file degli ordini", e);
        }
    }



    protected List<Order> getAllAssignedOrders() throws DAOException {

        return readFromFile(assignedOrdersFilePath);

    }

    protected void writeAssignedOrdersToFile(List<Order> assignedOrders) throws IOException {
        // Uso il metodo writeToFile della superclasse
        writeToFile(assignedOrders, assignedOrdersFilePath);

    }


    // metodo per deserializzare la lista di riders
    public List<Rider> getAllRiders() throws DAOException {

        return readFromFile(ridersFilePath);

    }

    protected List<User> readUsersFromFile() throws DAOException {
        // Usa il metodo readFromFile della superclasse, fornendo il percorso del file e il tipo atteso
        List<User> users = readFromFile(usersFilePath);
        // Verifico che tipo degli elementi sia corretto
        if (!users.isEmpty() && users.get(0) != null) {

            return users;
        } else {
            return new ArrayList<>();
        }
    }

    // Serializza gli utenti registrati nel file
    protected boolean writeUsersToFile(List<User> users) throws DAOException {
        try {
            writeToFile(users, usersFilePath);
            return true; //  true se la scrittura è andata a buon fine
        } catch (IOException e) {
            throw new DAOException("Errore nella scrittura del file degli ordini", e);
        }
    }

/* Convertendo IOException in DAOException, centralizzamoi la gestione degli errori legati ai dati. se poi si cambia il modo in cui i dati sono memorizzati (ad esempio, si va da file a database), non dobbiamo cambiare altre parti del codice che gestiscono DAOException
* tipo : Se cambio il metodo di salvataggio per usare un database, il metodo potrebbe iniziare a lanciare SQLException invece di IOException, se il resto del  codice gestisce
* solo DAOException, non andremo a cambiare quei gestori di eccezioni quando cambiamo il metodo di storage. Devo solo mettere che
*   le SQLException vengano convertite in DAOException nel DAO.*/






}
