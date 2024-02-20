package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.dao.OrderDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;

import java.util.ArrayList;
import java.util.List;



public class PendingOrdersController {
    // Metodo per ottenere un OrderListBean con gli ordini pendenti
    public OrderListBean getPendingOrderListBean() {
        // Chiamata al DAO per ottenere la lista di ordini pendenti
        DAOFactory daoFactory = new DAOFactory();

        OrderDAO orderDao = daoFactory.getOrderDAO();
        OrderList orderList = orderDao.getPendingOrders();

        OrderListBean orderListBean = new OrderListBean();

        // Creazione di una nuova lista vuota per gli OrderBean
        List<OrderBean> orderBeans = new ArrayList<>();

        // Ottieniamo la lista degli ordini dall'OrderList
        List<Order> orders = orderList.getOrders();


        for (Order order : orders) {
            OrderBean orderBean = convertToOrderBean(order);
            orderBeans.add(orderBean);
        }

        // setta la lista di OrderBean nel OrderListBean
        orderListBean.setOrderBeans(orderBeans);


        return orderListBean;
    }

    public OrderListBean getAssignedOrdersBean() {
        OrderDAO orderDAO = new DAOFactory().getOrderDAO();
        String resellerEmail = Session.getSession().getUser().getEmail();
        OrderList assignedOrders = orderDAO.getAssignedOrders(resellerEmail);

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

    public void updateOrderStatus (OrderBean orderBean){
        DAOFactory daoFactory = new DAOFactory();
        OrderDAO orderDao = daoFactory.getOrderDAO();
        orderDao.updateAvailability(orderBean);
    }

    public List<RiderBean> getAvalaibleRiders(SearchBean searchBean) {

        // Chiamata al DAO per ottenere la lista di ordini pendenti
        DAOFactory daoFactory = new DAOFactory();
        RiderDAO riderDao = daoFactory.getRiderDAO();
        List<Rider> availableRiders = riderDao.getAvailableRiders(searchBean);
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



