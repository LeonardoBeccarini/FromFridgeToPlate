package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.OrderBean;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.dao.NotificationDAO;
import com.example.fromfridgetoplate.logic.dao.OrderDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;

public class NotificationManager {

    private static NotificationManager instance = new NotificationManager();

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        return instance;
    }


    public void registerRiderAvailability(RiderBean riderBn) {

        DAOFactory daoFactory = new DAOFactory();
        RiderDAO riderDAO = daoFactory.getRiderDAO();

        riderDAO.setRiderAvailable(riderBn);

    }

    public void notifyRider(RiderBean riderBean, OrderBean orderBean) { // purtroppo da qui il rider, dovrà andarsi a retrievare
        // da solo le notifiche dalla tabella nel databse, dall'istanza di applicazione del rivenditore, non c'è modo di
        // contattare il rider in un'altra istanza di applicazione

        DAOFactory daoFactory = new DAOFactory();
        RiderDAO riderDAO = daoFactory.getRiderDAO();
        NotificationDAO ntfDAO = daoFactory.getNotificationDAO();

        boolean isAvailable = riderDAO.isRiderAvailable(riderBean);
        if (isAvailable) {
            // Crea una notifica nel database per il rider
            Order order = new Order(orderBean.getOrderId(), orderBean.getCustomerId(), orderBean.getShopId(), orderBean.getStatus(), orderBean.getOrderTime(), orderBean.getRiderId());
            order.setShippingCity(orderBean.getShippingAddress().getShippingCity());

            order.setShippingProvince(orderBean.getShippingAddress().getShippingProvince());
            order.setShippingStreet(orderBean.getShippingAddress().getShippingStreet());
            order.setShippingStreetNumber(orderBean.getShippingAddress().getShippingStreetNumber());
            ntfDAO.insertNotification(riderBean.getId(), order, "Nuovo ordine assegnato");

        }

        OrderDAO orderDAO = daoFactory.getOrderDAO();
        orderDAO.assignRiderToOrder(orderBean.getOrderId(), riderBean.getId());
        orderDAO.setAssignation(orderBean.getOrderId());

    }



}
