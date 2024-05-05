package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.dao.ResellerDAO;
import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;

import java.util.ArrayList;
import java.util.List;



public class PendingOrdersController {


    // Metodo per ottenere direttamente gli OrderBean aggiornati per demetra
    public List<OrderBean> getUpdatedPendingOrders() throws DAOException {

        return getPendingOrderListBean().getOrderBeans();
    }

    private OrderListBean getPendingOrderListBean() throws DAOException {
        // Chiamata al DAO per ottenere la lista di ordini pendenti

        DAOAbsFactory absFactory = DAOFactoryProvider.getInstance().getDaoFactory();

        ResellerDAO resellerDao = null;
        try {
            resellerDao = absFactory.createResellerDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException("Errore nella configurazione durante la creazione della ResellerDAO: " + e.getMessage(), e);
        }
        String loggedEmail = Session.getSession().getUser().getEmail();
        OrderList orderList = resellerDao.getPendingOrders(loggedEmail);
        OrderListBean orderListBean = new OrderListBean();

        // Creazione di una nuova lista vuota per gli OrderBean
        List<OrderBean> orderBeans = new ArrayList<>();
        // Ottieniamo la lista degli ordini dall'OrderList
        List<Order> orders = orderList.getOrders();

        for (Order order : orders) {
            OrderBean orderBean = convertToOrderBean(order);
            orderBeans.add(orderBean);
        }

        // setta la lista di OrderBean nell' OrderListBean
        orderListBean.setOrderBeans(orderBeans);


        return orderListBean;
    }

    public OrderListBean getAssignedOrdersBean() throws DAOException {


        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        ResellerDAO resellerDAO = null;
        try {
            resellerDAO = daoAbsFactory.createResellerDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException("Errore nella configurazione durante la creazione della resellerDAO: " + e.getMessage(), e);
        }
        String resellerEmail = Session.getSession().getUser().getEmail();
        OrderList assignedOrders = resellerDAO.getAssignedOrders(resellerEmail);

        OrderListBean orderListBean = new OrderListBean();
        for (Order order : assignedOrders.getOrders()) {
            OrderBean orderBean = new OrderBean(order.getOrderId(), order.getCustomerId(), order.getShopId(), order.getStatus(), order.getOrderTime(), order.getRiderId(), order.getShippingCity());
            orderListBean.getOrderBeans().add(orderBean);
        }

        return orderListBean;
    }


    private OrderBean convertToOrderBean(Order order) {
        OrderBean orderBean = new OrderBean();

        // Impostiamo i valori nel 'OrderBean usando i dati dall'istanza di Order
        orderBean.setOrderId(order.getOrderId());
        orderBean.setCustomerId(order.getCustomerId());
        orderBean.setFoodItems(order.getItems()); //  getItems() restituisca una lista di food_item
        orderBean.setOrderTime(order.getOrderTime());
        orderBean.setShippingCity(order.getShippingCity());

        AddressBean addrBean = new AddressBean(order.getShippingStreet(), order.getShippingStreetNumber(),order.getShippingCity(), order.getShippingProvince());
        orderBean.setShippingAddress(addrBean);

        return orderBean;
    }

    /**
     * L'implementazione della bean ci assicura che tutti i dati rilevanti dell'ordine (orderId, customerId, foodItems, orderTime)
     * vengano trasferiti dall'entità Order al Bean OrderBean.
     * cosi facciamo una separazione tra la rappresentazione
     * dei dati nell'ambito della logica di business (cioè l'entità Order) e la trasmissione
     * dei dati tra la logica di business e la parte grafica (Bean OrderBean).
     **/


    public List<RiderBean> getAvalaibleRiders(SearchBean searchBean) throws DAOException {

        // Chiamata al DAO per ottenere la lista di ordini pendenti
        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        ResellerDAO resellerDAO = null;
        try {
            resellerDAO = daoAbsFactory.createResellerDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException("Errore nella configurazione durante la creazione della ResellerDAO: " + e.getMessage(), e);
        }
        List<Rider> availableRiders = resellerDAO.getAvailableRiders(searchBean.getCity());
        List<RiderBean> avRidersBean = new ArrayList<>();
        // bisogna convertire  List <Rider> in List <RiderBean>
        for (Rider rider : availableRiders) {

            avRidersBean.add(convertToRiderBean(rider));

        }
        return avRidersBean;

    }

    private RiderBean convertToRiderBean(Rider rider) {

        if (rider == null) {
            return null;
        }

        RiderBean riderBean = new RiderBean(
                rider.getId(),
                rider.getName(),
                rider.getSurname(),
                rider.isAvailable(),
                rider.getAssignedCity()
        );
        riderBean.setId(rider.getId());
        return riderBean;
    }


}



