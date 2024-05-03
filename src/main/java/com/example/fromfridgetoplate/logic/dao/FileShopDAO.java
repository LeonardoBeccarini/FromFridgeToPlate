package com.example.fromfridgetoplate.logic.dao;


import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.Shop;
import com.example.fromfridgetoplate.logic.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileShopDAO extends FileDAOBase implements ShopDAO {

// x becca: dovresti cambiare nome e messaggio di errore alla DbException, in tipo PersistenceException in modo
// che non sia relativa solo al db ma anche a i file
    public FileShopDAO() {}

    @Override
    public boolean saveShop(Shop shop) throws DbException, DAOException {

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


    public Shop retrieveShopByEmail(String email) throws DbException {
        List<Shop> shops = readShopsFromFile();
        for (Shop shop : shops) {
            if (shop.getEmail().equals(email)) {
                return shop;
            }
        }
        return null;
    }

    public List<Shop> retrieveShopByName(String name) throws DbException {
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
    private List<Shop> readShopsFromFile() throws DbException {
        File file = new File(shopsFilePath);
        if (!file.exists() || file.length()==0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Shop>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); //debug
            throw new DbException("Errore durante la lettura dal file: " + e.getMessage());
        }
    }


    private void writeShopsToFile(List<Shop> shops) throws DbException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(shopsFilePath))) {
            oos.writeObject(shops);
        } catch (IOException e) {
            throw new DbException("Errore durante la scrittura sul file: " + e.getMessage());
        }
    }



    public static void main(String[] args) throws DAOException {
        FileShopDAO dao = new FileShopDAO();

        // Crea un nuovo negozio di esempio
        Shop newShop = new Shop("quircio5@gmail.com", "qr", "quircioresellero", "Via dei pantani", "12345678123", "1234567890");

        boolean registrationResult = false;
        try {
            registrationResult = dao.saveShop(newShop);
        } catch (DbException e) {
            throw new RuntimeException(e);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        if (registrationResult) {
            System.out.println("Il nuovo negozio è stato registrato con successo.");
        } else {
            System.out.println("La registrazione del nuovo negozio non è riuscita (potrebbe essere già registrato).");
        }

        // Stampa tutti i negozi per verificare la registrazione
        System.out.println("\nElenco dei negozi registrati:");
        try {
            for (Shop shop : dao.readShopsFromFile()) {
                System.out.println("Email: " + shop.getEmail() + ", Nome: " + shop.getName() + " vat: " + shop.getVATnumber());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        // Leggi e stampa tutti gli utenti dal file "users.dat" per verificare che il nuovo negozio sia stato aggiunto come owner
        System.out.println("\nElenco degli utenti registrati in 'users.dat':");

        List<User> users = dao.readUsersFromFile();
        for (User user : users) {
            System.out.println("Email: " + user.getEmail() + ", Ruolo: " + user.getRole());
        }

    }




}

