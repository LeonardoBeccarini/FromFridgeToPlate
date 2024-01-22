package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.dao.OrderDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.model.FoodItem;
import com.example.fromfridgetoplate.logic.model.Order;
import com.example.fromfridgetoplate.logic.model.OrderList;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;

import java.util.ArrayList;
import java.util.List;



public class PendingOrdersController {
    // Metodo per ottenere un OrderListBean con gli ordini pendenti
    public OrderListBean getPendingOrderListBean() {
        // Chiamata al DAO per ottenere la lista di ordini pendenti
        DAOFactory daoFactory = new DAOFactory();

        OrderDAO order_dao = daoFactory.getOrderDAO();
        OrderList orderList = order_dao.getPendingOrders();

        //BaseDAO<Void, OrderList> order_dao = daoFactory.getOrderDAO();
        //OrderList orderList = order_dao.get(null);


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

    public void update_orderStatus (OrderBean order_bean){
        DAOFactory daoFactory = new DAOFactory();
        OrderDAO order_dao = daoFactory.getOrderDAO();
        order_dao.update_availability(order_bean);
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
                rider.getName(),
                rider.getSurname(),
                rider.isAvailable(),
                rider.getAssignedCity()
        );
        riderBean.setId(rider.getId());
        return riderBean;
    }












/*

    // main per provare se i dati sono giusti prima di passarli alla view
    public static void main(String[] args) {
        PendingOrdersController controller = new PendingOrdersController();
        OrderListBean orderListBean = controller.getPendingOrderListBean();

        for (OrderBean orderBean : orderListBean.getOrderBeans()) {

            System.out.println("Order ID: " + orderBean.getOrderId());
            System.out.println("Customer ID: " + orderBean.getCustomerId());
            System.out.println("Order Time: " + orderBean.getOrderTime());

            // Stampa gli food_items per l'ordine
            System.out.println("Food Items per l'ordine con order_id " + orderBean.getOrderId() + " :");
            for (FoodItem item : orderBean.getFoodItems()) {
                System.out.println(" - Name: " + item.getName() + ", Quantity: " + item.getQuantity());
            }
            System.out.println("-------------------------------------");
        }

        RiderPrefBean rpb = new RiderPrefBean("New York");
        List<RiderBean> avRidersBean = controller.getAvalaibleRiders(rpb);

        for (RiderBean rider_bean : avRidersBean) {
            System.out.println("Rider ID: " + rider_bean.getId() +
                    ", Name: " + rider_bean.getName() +
                    ", Surname: " + rider_bean.getSurname() +
                    ", Assigned City: " + rider_bean.getAssignedCity());
        }



    }
*/
}



