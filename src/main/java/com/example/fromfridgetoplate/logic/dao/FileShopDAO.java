package com.example.fromfridgetoplate.logic.dao;


import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.Shop;
import com.example.fromfridgetoplate.logic.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileShopDAO extends FileDAOBase implements ShopDAO {

    public FileShopDAO() throws ConfigurationException {
        super();
    }

    // x becca: dovresti cambiare nome e messaggio di errore alla DbException, in tipo PersistenceException in modo
// che non sia relativa solo al db ma anche a i file
    @Override
    public boolean saveShop(Shop shop) throws DAOException {

            List<User> users = readUsersFromFile();
            for (User user : users) {
                if (user.getEmail().equals(shop.getEmail())) {
                    // L'utente esiste già
                    return false;
                }
            }
            users.add(new User(shop.getEmail(), shop.getPassword(), Role.OWNER));
            writeUsersToFile(users);

            List<Shop> shops = readShopsFromFile(); // lancia DBException
            for (Shop existingShop : shops) {
                if (existingShop.getEmail().equals(shop.getEmail()) || existingShop.getVATnumber().equals(shop.getVATnumber())) {
                    // Il negozio esiste già
                    return false;
                }
            }
            shops.add(shop);
            writeShopsToFile(shops);

            return true;

    }


    public Shop retrieveShopByEmail(String email) throws DAOException {
        List<Shop> shops = readShopsFromFile();
        for (Shop shop : shops) {
            if (shop.getEmail().equals(email)) {
                return shop;
            }
        }
        return null;
    }

    public List<Shop> retrieveShopByName(String name) throws DAOException {
        List<Shop> shops = readShopsFromFile();
        List<Shop> result = new ArrayList<>();
        for (Shop shop : shops) {
            if (shop.getName().equalsIgnoreCase(name)) {
                result.add(shop);
            }
        }
        return result;
    }

    // Helper method to read shops from file
    private List<Shop> readShopsFromFile() throws DAOException {
        File file = new File(shopsFilePath);
        if (!file.exists() || file.length()==0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Shop>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {

            throw new DAOException("Errore durante la lettura dal file: " + e.getMessage());
        }
    }


    private void writeShopsToFile(List<Shop> shops) throws DAOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(shopsFilePath))) {
            oos.writeObject(shops);
        } catch (IOException e) {
            throw new DAOException("Errore durante la scrittura sul file: " + e.getMessage());
        }
    }

}

