package com.example.fromfridgetoplate.logic.dao;


import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.*;

import java.io.*;
import java.util.*;


public class FileRiderDAO extends FileDAOBase implements RiderDAO {


    public FileRiderDAO() throws ConfigurationException {
        super();
    }

    public void setRiderAvailable(int riderId, boolean isAval) throws DAOException {
        List<Rider> riders = getAllRiders(); // deserializzo tutti i rider

        // Trovo il rider con l'ID specificato e aggiorno la sua disponibilità
        for (Rider rider : riders) {

            if (rider.getId() == riderId) {
                rider.setAvailable(isAval);
                break; // Interrompo  una volta trovato e aggiornato il rider
            }
        }


        // Riserializzo la lista aggiornata di rider nel file
        writeRidersToFile(riders);
    }




    public Rider getRiderDetailsFromSession() throws DAOException{
        // Questo metodo presuppone che tu abbia un modo per ottenere l'email dell'utente corrente
        String userEmail = Session.getSession().getUser().getEmail(); // con "Session.getSession().getUser()" ricavo il current User,
        //questo contiene le informazione immesse al momento del login, quindi username, pw, e role, poi ne prendo l'email (usernm) cosi
        // da poter accedere alle informazioni immesse al momento della registrazione, che mi servono, per popolare il riderbean,
        // che verrà poi passata dal controlle grafico del rider sia a quello applicativo sia al controller delle notifiche

        List<Rider> allRiders = getAllRiders();

        for (Rider rider : allRiders) {
            if (rider.getEmail().equals(userEmail)) {
                return rider;
            }
        }

        return null; // Restituisce null se non viene trovato nessun rider con l'email specificata
    }


    public boolean registerRider(Rider newRider) throws DAOException{
        // Controllo se esiste già un utente (rider o altro) con la stessa email
        if (isUserExists(newRider.getEmail())) {

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

    private boolean isUserExists(String email) throws DAOException {
        List<User> users = readUsersFromFile();
        for (User user : users) {
            if (user.getEmail().equals(email)) {

                return true; // L'utente esiste già
            }
        }
        return false; // Nessun utente corrispondente trovato
    }

    private int getMaxRiderId() throws DAOException {
        List<Rider> riders = getAllRiders();
        int maxId = 0;
        for (Rider rider : riders) {
            if (rider.getId() > maxId) {
                maxId = rider.getId();
            }
        }
        return maxId;
    }



    private boolean addUserToFile(User user) throws DAOException{
        List<User> users = readUsersFromFile();
        users.add(user);
        return writeUsersToFile(users);
    }



    public boolean addRiderToFile(Rider rider) throws DAOException {
        List<Rider> riders = getAllRiders();
        riders.add(rider);
        return writeRidersToFile(riders);
    }




    // il metodo ha la responsabilità di serializzare la lista di oggetti Rider in un file.
    private boolean writeRidersToFile(List<Rider> riders) throws DAOException {
        try {
            // uso il metodo writeToFile della superclasse per scrivere i rider nel file
            writeToFile(riders, ridersFilePath);
            return true; //  true sea scrittura è andata a buon fine
        } catch (IOException e) {
            // Rilancio l'eccezione come DAOException
            throw new DAOException("Errore nella scrittura del file degli ordini", e);
        }
    }
    /* Convertendo IOException in DAOException, centralizzamoi la gestione degli errori legati ai dati. se poi si cambia il modo in cui i dati sono memorizzati (ad esempio, si va da file a database), non dobbiamo cambiare altre parti del codice che gestiscono DAOException
     * tipo :Se cambio il metodo di salvataggio per usare un database, il metodo potrebbe iniziare a lanciare SQLException invece di IOException, se il resto del  codice gestisce
     * solo DAOException, non andremo a cambiare quei gestori di eccezioni quando cambiamo il metodo di storage. Devo solo mettere che
     *   le SQLException vengano convertite in DAOException nel  DAO.*/

    public void setRidersFilePath(String filePath) {
        this.ridersFilePath = filePath;
    }
    public String getRidersFilePath() {
        return this.ridersFilePath;
    }

}












