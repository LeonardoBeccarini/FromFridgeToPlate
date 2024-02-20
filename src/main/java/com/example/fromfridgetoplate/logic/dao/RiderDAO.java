package com.example.fromfridgetoplate.logic.dao;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.exceptions.DeliveryRetrievalException;
import com.example.fromfridgetoplate.logic.exceptions.OrderNotFoundException;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.logic.model.Session;

import java.sql.*;
import java.time.LocalDateTime;
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
            System.out.println("cityrider:" + rpBean.getCity() );

            rs = cstmt.executeQuery();



            while (rs.next()) {
                int riderId = rs.getInt("Id");
                System.out.println("riderId:"+ riderId);
                Rider rider = new Rider(
                        riderId,
                        rs.getString("Nome"),
                        rs.getString("Cognome"),
                        rs.getString("assignedCity")

                );
                // non sono inizializzati dal costruttore, perchè quando
                // viene creato un rider, si suppone che quei campi potrebbero ancora non essere decisi al momento della creazione
                // rider.setAvailable(rs.getBoolean("isAvailable")); // questo sarà sempre true
                //rider.setAssignedCity(rs.getString("assignedCity"));
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
        //System.out.println("useremail: "+ userEmail);
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
                    //System.out.println("riderId from details: "+ rs.getInt("Id") );
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


    public void acceptOrder(int orderId, int riderId) {
        String query = "{CALL AcceptOrder(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, orderId);
            cstmt.setInt(2, riderId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // da moddare bene poi
        }

    }

    public void declineOrder(int orderId, int riderId) {
        String query = "{CALL DeclineOrder(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, orderId);
            cstmt.setInt(2, riderId);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // da moddare bene poi
        }
    }


    public OrderList getConfirmedDeliveriesForRider(int riderId) throws DeliveryRetrievalException {
        OrderList confirmedDeliveries = new OrderList();

        String call = "{CALL GetConfirmedDeliveriesForRider(?)}";
        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setInt(1, riderId);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getInt("orderId"),
                            rs.getString("CustomerId"),
                            rs.getString("NegozioId"),
                            rs.getString("status"),
                            rs.getTimestamp("orderTime").toLocalDateTime(),
                            rs.getInt("RiderId")
                    );
                    order.setDeliveryTime(rs.getTimestamp("deliveryTime").toLocalDateTime());
                    order.setShippingStreet(rs.getString("shippingStreet"));
                    order.setShippingStreetNumber(rs.getInt("shippingStreetNumber"));
                    order.setShippingCity(rs.getString("shippingCity"));
                    order.setShippingProvince(rs.getString("shippingProvince"));
                    // Impostare altre proprietà di Order come necessario...

                    confirmedDeliveries.addOrder(order);
                }
            }
        } catch (SQLException e) {
            throw new DeliveryRetrievalException("Errore nel recupero delle consegne confermate per il rider con ID: " + riderId, e);
        }

        return confirmedDeliveries;
    }


    public boolean checkForOrderInDelivery(int riderId) {
        String query = "{CALL checkOrderInDeliveryForRider(?, ?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderId);
            cstmt.registerOutParameter(2, Types.BOOLEAN);
            cstmt.execute();

            return cstmt.getBoolean(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Order getInDeliveryOrderForRider(int riderId) throws OrderNotFoundException {
        Order order = null;
        String query = "{CALL GetInDeliveryOrderForRider(?)}";
        try (CallableStatement cstmt = connection.prepareCall(query)) {
            cstmt.setInt(1, riderId);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    order = new Order(rs.getInt("orderId"), rs.getString("shippingStreet"), rs.getInt("shippingStreetNumber"), rs.getString("shippingCity"), rs.getString("shippingProvince"));
                } else {
                    // perchèse il resultSet è vuoto, significa che non è stato trovato alcun ordine 'in consegna' per il rider
                    throw new OrderNotFoundException("Nessun ordine 'in consegna' trovato");
                }
            }
        } catch (SQLException e) {

            throw new OrderNotFoundException("Errore nel recupero dell'ordine");
        }
        return order;
    }


    public void updateOrderStatusToDelivered(int orderId, LocalDateTime deliveryTime) {
        String query = "{CALL UpdateOrderToDelivered(?, ?)}";
        try {
            CallableStatement cstmt = connection.prepareCall(query);
            cstmt.setInt(1, orderId);
            cstmt.setTimestamp(2, Timestamp.valueOf(deliveryTime));
            cstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean registerRider(String name, String surname, String email, String password, String city) {
        CallableStatement stmt = null;
        try {
            System.out.println("name"+ name + "pw" + password);
            stmt = connection.prepareCall("{CALL RegisterRider(?, ?, ?, ?, ?, ?)}");

            stmt.setString(1, name);
            stmt.setString(2, surname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, city);


            stmt.registerOutParameter(6, Types.BOOLEAN);

            stmt.execute();

            boolean result = stmt.getBoolean(6);

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            // Chiudi le risorse JDBC in modo sicuro
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }






}



