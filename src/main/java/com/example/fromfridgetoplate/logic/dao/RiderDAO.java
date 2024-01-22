package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.bean.RiderPrefBean;
import com.example.fromfridgetoplate.logic.bean.SearchBean;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.logic.model.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RiderDAO {
    private Connection connection;

    public RiderDAO(Connection connection) {
        this.connection = connection;
    }

    // Metodo per ottenere i rider disponibili
    public List<Rider> getAvailableRiders(SearchBean rpBean) {
        List<Rider> availableRiders = new ArrayList<>();
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            cstmt = connection.prepareCall("{CALL GetAvailableRiders(?)}");// la stored procedure ritornerà un result_set con
            // i riders operanti in quella città(indiciata da pBean.getCity())
            cstmt.setString(1, rpBean.getCity());

            rs = cstmt.executeQuery();

            while (rs.next()) {
                Rider rider = new Rider(
                        rs.getInt("Id"),
                        rs.getString("Email"),
                        rs.getString("U_Password"),
                        rs.getString("Nome"),
                        rs.getString("Cognome")
                );
                // non sono inizializzati dal costruttore, perchè quando
                // viene creato un rider, si suppone che quei campi potrebbero ancora non essere decisi al momento della creazione
                // rider.setAvailable(rs.getBoolean("isAvailable")); // questo sarà sempre true
                rider.setAssignedCity(rs.getString("assignedCity"));
                availableRiders.add(rider);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestione delle eccezioni
        } finally {

            closeQuietly(rs);
            closeQuietly(cstmt);
        }

        return availableRiders;
    }

    //public void updateA

    public void setRiderAvailable(RiderBean riderBn) {
        String query = "{CALL SetRiderAvailability(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderBn.getId());
            cstmt.setBoolean(2, riderBn.isAvailable());
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    public boolean isRiderAvailable(RiderBean riderBn) {
        String query = "{CALL GetRiderAvailability(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderBn.getId());
            cstmt.registerOutParameter(2, Types.BOOLEAN);
            cstmt.execute();

            return cstmt.getBoolean(2);
        } catch (SQLException e) {
            e.printStackTrace();
            // sempre da combiare poi
        }
        return false;
    }
// MANCA DA IMPLEMENTARE LA STORED PROCEDURE E IL CODICE PER REGISTRARE UN RIDER , QUI SI SUPPONE CHE NELLA TABELLA
    // RIDER SIA PRESENTE UN ENTRY CON EMAIL UGUALE (LHO FATTO A MANO NEL WORKBENCH) A QUELLA CON CUI IL RIDER SI è
    // REGISTRATO , E CON CUI EFFETTUA L'ACCESSO, BISOGNA FARE LA REGISTRAZIONE DEL RIDER, DOVE PROCEDURA VA AD INSERIRE
    // LA TUPL APRIMA NELLA TABELLA USER E POI NELLA TABELLA RIDER, COME FATTO NELLE PROCEDURES REGISTERSHOP E RREGISTERCLIENT
    public RiderBean getRiderDetailsFromSession() {
        String userEmail = Session.getSession().getUser().getEmail(); // con "Session.getSession().getUser()" ricavo il current User,
        //questo contiene le informazione immesse al momento del login, quindi username, pw, e role, poi ne prendo l'email (usernm) cosi
        // da poter accedere alle informazioni immesse al momento della registrazione, che mi servono, per popolare il riderbean,
        // che verrà poi passata dal controlle grafico del rider sia a quello applicativo sia al controller delle notifiche
        System.out.println("useremail: "+ userEmail);
        String query = "{CALL GetRiderDetailsByEmail(?)}";

        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setString(1, userEmail);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    RiderBean riderBean = new RiderBean(
                            rs.getString("Nome"),
                            rs.getString("Cognome"),
                            rs.getBoolean("isAvailable"),
                            rs.getString("assignedCity")
                    );

                    riderBean.setId(rs.getInt("Id"));
                    return riderBean;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }






    // Metodo per chiudere le risorse in modo sicuro
    void closeQuietly(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                // Log dell'eccezione silenziosa se necessario
                e.printStackTrace();
            }
        }
    }







}



