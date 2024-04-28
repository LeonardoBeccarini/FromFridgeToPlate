package com.example.fromfridgetoplate.logic.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
public class FileTest {

    public static void main(String[] args) {
        String filePath = "testData.ser";

        //  lista  interi da scrivere nel file
        List<Integer> integersToWrite = Arrays.asList(1, 2, 3, 4, 5);


        FileTest fileManager = new FileTest();

        // Scrittura lista nel file
        fileManager.writeToFile(integersToWrite, filePath);

        // Lettura della lista dal file
        List<Integer> integersRead = fileManager.readFromFile(filePath);


        System.out.println("Contenuti letti dal file:");
        for (Integer number : integersRead) {
            System.out.println(number);
        }
    }


    protected static <T> void writeToFile(List<T> genericList, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(genericList);
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura nel file: " + e.getMessage());
            e.printStackTrace();
        }
    }



    protected <T> List<T> readFromFile(String filePath) {
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

            System.err.println("eof raggiunta" + e.getMessage());
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("io exc " + e.getMessage());
            return new ArrayList<>();
        }
    }
}*/

import com.example.fromfridgetoplate.logic.model.User;
import com.example.fromfridgetoplate.logic.model.Role;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTest {

    private static String filePath = "userData.ser";

    public static void main(String[] args) {
        // Creazione di una lista di oggetti User da scrivere nel file
        List<User> usersToWrite = Arrays.asList(
                new User("esempio1@example.com", "password123", Role.CLIENT),
                new User("esempio2@example.com", "password456", Role.CLIENT)
        );

        // Scrittura della lista nel file
        writeToFile(usersToWrite, filePath);

        // Lettura della lista
        List<User> usersRead = readFromFile(filePath);


        System.out.println("Contenuti letti dal file:");
        for (User user : usersRead) {
            System.out.println("Email: " + user.getEmail() + ", Role: " + user.getRole());
        }

    }

    protected static <T> void writeToFile(List<T> genericList, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(genericList);
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura nel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected static <T> List<T> readFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List) {
                return (List<T>) readObject;
            } else {
                System.err.println("Tipo di dati scritti male nel file: non è una lista.");
                return new ArrayList<>();
            }
        } catch (EOFException e) {
            System.err.println("EOF raggiunta: " + e.getMessage());
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante la lettura dal file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

