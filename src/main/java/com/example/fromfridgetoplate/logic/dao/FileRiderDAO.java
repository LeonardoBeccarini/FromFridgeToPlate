package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.model.*;

import java.io.*;
import java.util.*;


public class FileRiderDAO extends FileDAOBase implements RiderDAO {


    public FileRiderDAO() {}

    public void setRiderAvailable(RiderBean riderBn) {
        List<Rider> riders = getAllRiders(); // deserializzo tutti i rider

        // Trovo il rider con l'ID specificato e aggiorno la sua disponibilità
        for (Rider rider : riders) {
            if (rider.getId() == riderBn.getId()) {
                rider.setAvailable(riderBn.isAvailable());
                break; // Interrompo  una volta trovato e aggiornato il rider
            }
        }


        // Riserializzo la lista aggiornata di rider nel file
        writeRidersToFile(riders);
    }




    public RiderBean getRiderDetailsFromSession() {
        // Questo metodo presuppone che tu abbia un modo per ottenere l'email dell'utente corrente
        String userEmail = Session.getSession().getUser().getEmail(); // con "Session.getSession().getUser()" ricavo il current User,
        //questo contiene le informazione immesse al momento del login, quindi username, pw, e role, poi ne prendo l'email (usernm) cosi
        // da poter accedere alle informazioni immesse al momento della registrazione, che mi servono, per popolare il riderbean,
        // che verrà poi passata dal controlle grafico del rider sia a quello applicativo sia al controller delle notifiche

        List<Rider> allRiders = getAllRiders();

        for (Rider rider : allRiders) {
            if (rider.getEmail().equals(userEmail)) {

                RiderBean riderBean = new RiderBean(
                        rider.getName(),
                        rider.getSurname(),
                        rider.isAvailable(),
                        rider.getAssignedCity()
                );
                riderBean.setId(rider.getId());
                return riderBean;
            }
        }

        return null; // Restituisce null se non viene trovato nessun rider con l'email specificata
    }


    public boolean registerRider(Rider newRider) {
        // Controllo se esiste già un utente (rider o altro) con la stessa email
        if (isUserExists(newRider.getEmail())) {
            System.out.println("rider gia registrato");
            return false; // L'utente è già registrato
        }

        // Aggiungo l'utente come credenziali al file degli utenti
        if (!addUserToFile(new User(newRider.getEmail(), newRider.getPassword(), Role.RIDER))) {
            return false;
        }

        // Determino l'ID massimo corrente tra i rider cosi da assegnare un id incrementale al nuovo rider, (simulo l'autoincrement della procedura mysql)
        int maxRiderId = getMaxRiderId();
        newRider.setId(maxRiderId + 1); // Assegno ID incrementale

        // Imposto imnizialmente il rider come disponibile, se non già impostato
        newRider.setAvailable(true);

        // Aggiungo ora il nuovo rider al file dei rider
        return addRiderToFile(newRider);
    }

    private boolean isUserExists(String email) {
        List<User> users = readUsersFromFile();
        for (User user : users) {
            if (user.getEmail().equals(email)) {

                return true; // L'utente esiste già
            }
        }
        return false; // Nessun utente corrispondente trovato
    }

    private int getMaxRiderId() {
        List<Rider> riders = getAllRiders();
        int maxId = 0;
        for (Rider rider : riders) {
            if (rider.getId() > maxId) {
                maxId = rider.getId();
            }
        }
        return maxId;
    }



    private boolean addUserToFile(User user) {
        List<User> users = readUsersFromFile();
        users.add(user);
        return writeUsersToFile(users);
    }



    private boolean addRiderToFile(Rider rider) {
        List<Rider> riders = getAllRiders();
        riders.add(rider);
        return writeRidersToFile(riders);
    }




    // il metodo ha la responsabilità di serializzare la lista di oggetti Rider in un file.
    private boolean writeRidersToFile(List<Rider> riders) {
        // Usa il metodo writeToFile della superclasse per scrivere i rider nel file
        writeToFile(riders, ridersFilePath);
        return true;
    }


    public static void main(String[] args) {
        FileRiderDAO dao = new FileRiderDAO();
        // rider di esempio
        Rider newRider = new Rider( "marco2@gmail.com", "marco", "mirini", "marco2", "Milano");
        boolean registrationResult = dao.registerRider(newRider);

        if (registrationResult) {
            System.out.println("Il nuovo rider è stato registrato con successo.");
        } else {
            System.out.println("La registrazione del nuovo rider non è riuscita (potrebbe essere già registrato).");
        }

        // verifica la registrazione
        System.out.println("\nElenco dei rider registrati:");
        for (Rider rider : dao.getAllRiders()) {
            System.out.println("Email: " + rider.getEmail() + ", ID: " + rider.getId() + ", Nome: " + rider.getName());
        }

        // stampo tutti gli utenti dal file "users.dat" per verificare che il nuovo rider sia stato aggiunto
        System.out.println("\nElenco degli utenti registrati in 'users.dat':");
        List<User> users = dao.readUsersFromFile();
        for (User user : users) {
            System.out.println("Email: " + user.getEmail() + ", Ruolo: " + user.getRole());
        }
    }






}








