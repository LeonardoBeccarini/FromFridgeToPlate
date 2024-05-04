package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.OrderAssignmentException;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;


public class NotificationManager {



    public void registerRiderAvailability(RiderBean riderBn) throws DAOException {


        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        RiderDAO riderDAO = null;
        try {
            riderDAO = daoAbsFactory.createRiderDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException("Errore nella configurazione durante la creazione della RiderDAO: " + e.getMessage(), e);
        }

        riderDAO.setRiderAvailable(riderBn.getId(), riderBn.isAvailable());

    }



    public void notifyRider(RiderBean riderBean, OrderBean orderBean) throws OrderAssignmentException, DAOException { // purtroppo da qui il rider, dovrà andarsi a retrievare
        // da solo le notifiche dalla tabella nel database, dall'istanza di applicazione del rivenditore, non c'è modo di
        // contattare il rider in un'altra istanza di applicazione

        NotificationDAO ntfDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory(); // qui avviene lo switch, il resto del codice rimane uguale
        ResellerDAO resellerDAO = null;
        try {
            resellerDAO = daoAbsFactory.createResellerDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException("Errore nella configurazione durante la creazione della RiderDAO: " + e.getMessage(), e);
        }

        boolean isAvailable = resellerDAO.isRiderAvailable(riderBean);
        if (isAvailable) {
            // Crea una notifica nel database per il rider
            Order order = new Order(orderBean.getOrderId(), orderBean.getCustomerId(), orderBean.getShopId(), orderBean.getStatus(), orderBean.getOrderTime(), orderBean.getRiderId());
            order.setShippingCity(orderBean.getShippingAddress().getShippingCity());

            order.setShippingProvince(orderBean.getShippingAddress().getShippingProvince());
            order.setShippingStreet(orderBean.getShippingAddress().getShippingStreet());
            order.setShippingStreetNumber(orderBean.getShippingAddress().getShippingStreetNumber());
            ntfDAO.insertNotification(riderBean.getId(), order, "Nuovo ordine assegnato");

        }


        resellerDAO.assignRiderToOrder(orderBean.getOrderId(), riderBean.getId());
        resellerDAO.setAssignation(orderBean.getOrderId());

    }



}
