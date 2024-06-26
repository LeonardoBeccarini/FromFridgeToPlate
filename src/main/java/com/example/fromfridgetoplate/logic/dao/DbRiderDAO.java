package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.logic.model.Session;

import java.sql.*;


public class DbRiderDAO implements RiderDAO {
    private Connection connection;

    public DbRiderDAO(Connection connection) {
        this.connection = connection;
    }

    public void setRiderAvailable(int riderId, boolean isAval) throws DAOException {
        String query = "{CALL SetRiderAvailability(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderId);
            cstmt.setBoolean(2, isAval);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Errore nell'impostare la disponibilità del rider.", e);
        }
    }




    //(FATTO) MANCA DA IMPLEMENTARE LA STORED PROCEDURE E IL CODICE PER REGISTRARE UN RIDER , QUI SI SUPPONE CHE NELLA TABELLA
    // RIDER SIA PRESENTE UN ENTRY CON EMAIL UGUALE (LHO FATTO A MANO NEL WORKBENCH) A QUELLA CON CUI IL RIDER SI è
    // REGISTRATO , E CON CUI EFFETTUA L'ACCESSO, BISOGNA FARE LA REGISTRAZIONE DEL RIDER, DOVE PROCEDURA VA AD INSERIRE
    // LA TUPL APRIMA NELLA TABELLA USER E POI NELLA TABELLA RIDER, COME FATTO NELLE PROCEDURES REGISTERSHOP E RREGISTERCLIENT(FATTO)
    public Rider getRiderDetailsFromSession() throws DAOException {
        String userEmail = Session.getSession().getUser().getEmail(); // con "Session.getSession().getUser()" ricavo il current User,
        //questo contiene le informazione immesse al momento del login, quindi username, pw, e role, poi ne prendo l'email (usernm) cosi
        // da poter accedere alle informazioni immesse al momento della registrazione, che mi servono, per popolare il riderbean,
        // che verrà poi passata dal controlle grafico del rider sia a quello applicativo sia al controller delle notifiche

        String query = "{CALL GetRiderDetailsByEmail(?)}";

        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setString(1, userEmail);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    return new Rider(
                            rs.getInt("Id"),
                            rs.getString("Nome"),
                            rs.getString("Cognome"),
                            rs.getBoolean("isAvailable"),
                            rs.getString("assignedCity")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nel recupero dei dettagli del rider dalla sessione.", e);
        }
        return null;
    }


    public boolean registerRider(Rider rider) throws DAOException {

        String qry = "{CALL RegisterRider(?, ?, ?, ?, ?, ?)}";
        // Utilizzo del try-with-resources per gestire automaticamente la chiusura del CallableStatement
        try (CallableStatement stmt = connection.prepareCall(qry)) {

            // Utilizza i getter dell'oggetto Rider per impostare i parametri della procedura
            stmt.setString(1, rider.getName());
            stmt.setString(2, rider.getSurname());
            stmt.setString(3, rider.getEmail());
            stmt.setString(4, rider.getPassword()); // Assicurati che Rider abbia un campo e un getter per la password
            stmt.setString(5, rider.getAssignedCity());

            stmt.registerOutParameter(6, Types.BOOLEAN);
            stmt.execute();

            return stmt.getBoolean(6);
        } catch (SQLException e) {
            throw new DAOException("Errore durante la registrazione del rider: " + e.getMessage(), e);
        }
    }




}



