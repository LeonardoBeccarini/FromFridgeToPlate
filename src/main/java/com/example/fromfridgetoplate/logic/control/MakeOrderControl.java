package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.boundary.DummyPaymentBoundary;
import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.logic.exceptions.*;
import com.example.fromfridgetoplate.logic.model.*;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MakeOrderControl {

    private final Catalog catalog;
    private final Cart cart = Session.getSession().getCart();
    private final CouponApplier couponApplier = new CouponApplier(cart);

    public MakeOrderControl(ShopBean shopBean) throws DAOException, IOException, EmptyCatalogException {
        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        CatalogDAO catalogDAO;
        try {
            catalogDAO = daoAbsFactory.createCatalogDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException("Errore nella configurazione durante la creazione della catalogDAO: " + e.getMessage());
        }

        Catalog catalogTemp = catalogDAO.retrieveCatalog(shopBean.getVatNumber());

       if(catalogTemp.getItems().isEmpty()){
           throw new EmptyCatalogException("catalog of selected shop is empty!");
       }else this.catalog = catalogTemp;
    }

    public MakeOrderControl() {
        this.catalog = null;
    }

    public List<ShopBean> loadShop(ShopSearchInfoBean shopSearchInfoBean) throws DAOException, ShopNotFoundException {
        List<ShopBean> listShopBean = new ArrayList<>();

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        ShopDAO shopDAO;
        try {
            shopDAO = daoAbsFactory.createShopDAO();
        } catch (ConfigurationException e) {
            throw new DAOException("Errore nella configurazione durante la creazione della ShopDAO: " + e.getMessage());
        }
        List<Shop> shopList = shopDAO.retrieveShopByName(shopSearchInfoBean.getName());
        if(shopList.isEmpty()){
            throw new ShopNotFoundException("No shop found!");
        }
        for(Shop shop: shopList ){
            ShopBean shopBean = new ShopBean(shop.getName(), shop.getAddress(), shop.getPhoneNumber(), shop.getVATnumber());
            listShopBean.add(shopBean);
        }

        return listShopBean;
    }


    public FoodItemListBean loadProducts(){
        FoodItemListBean foodItemListBean = new FoodItemListBean();
        for(FoodItem foodItem : catalog.getItems() ){
            FoodItemBean foodItemBean = new FoodItemBean(foodItem.getName(), foodItem.getPrice());
            foodItemListBean.addFoodItem(foodItemBean);
        }
        return foodItemListBean;
    }


    public FoodItemListBean searchProduct(FoodItemBean foodItemBean){
        List<FoodItem> filteredList = catalog.filterByName(foodItemBean.getName());
        FoodItemListBean filteredListBean = new FoodItemListBean();
        for(FoodItem foodItem : filteredList){
            filteredListBean.addFoodItem(new FoodItemBean(foodItem.getName(), foodItem.getPrice()));
        }
        return filteredListBean;
    }

    public void addToCart(FoodItemBean foodItemBean) {
        Objects.requireNonNull(cart).addItem(new CartItem(foodItemBean.getName(), foodItemBean.getPrice(), 1));
    }

    public void changeQuantity(CartItemBean cartItemBean, boolean isPlus){
        CartItem cartItem = cart.verifyPresence(cartItemBean.getName());
        if(isPlus){
            cart.addItem(cartItem);
        }
        else{
            cart.removeItem(cartItem);
        }

    }
    public CartBean loadCart(){
        ArrayList<CartItemBean> itemListBean = new ArrayList<>();
        for(CartItem item: cart.getItemList()){
            itemListBean.add(new CartItemBean(item.getName(), item.getPrice(), item.getQuantity()));
        }
        return new CartBean(itemListBean);
    }


    public TotalPriceBean applyCoupon(CouponBean couponBean) throws CouponNotFoundException, DAOException, NegativePriceException {
        CouponDAO couponDAO = new CouponDAO();
        Coupon retrievedCoupon =  couponDAO.retrieveCoupon(couponBean.getVatNumber(), couponBean.getCode());
        couponApplier.applyCoupon(retrievedCoupon);
        List<CouponBean> couponBeanList = convertToCouponBean(couponApplier.getCouponList());
        return  new TotalPriceBean(couponApplier.getFinalPrice().getPrice(), couponBeanList);

    }

    public TotalPriceBean getOriginalPrice() {
        return new TotalPriceBean(couponApplier.getFinalPrice().getPrice());
    }

    private List<CouponBean> convertToCouponBean(CouponList couponList){
        List<CouponBean> couponBeanList= new ArrayList<>();
        for (Coupon coupon : couponList.getCouponList()){
            CouponBean couponBean = new CouponBean(coupon.getCode());
            couponBeanList.add(couponBean);
        }
        return  couponBeanList;
    }

    public void completeOrder(OrderBean orderBean) throws  PaymentFailedException, DAOException, IOException {

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        OrderDAO orderDAO;
        try {
            orderDAO = daoAbsFactory.createOrderDAO();
        } catch (ConfigurationException e) {
            throw new DAOException("Errore nella configurazione durante la creazione della OrderDAO: " + e.getMessage(), e);
        }
        NotificationDAO notificationDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());
        CouponDAO couponDAO = new CouponDAO();

        // elimino i coupon usati che stanno nella couponList del couponApplier
        CouponList couponList = couponApplier.getCouponList();
        for(Coupon coupon: couponList.getCouponList()){
            couponDAO.deleteCoupon(orderBean.getShopId(), coupon.getCode());
        }
        AddressBean addressBean = orderBean.getShippingAddress();
        String customerId = Session.getSession().getUser().getEmail();
        DummyPaymentBoundary dummyPaymentBoundary = new DummyPaymentBoundary();
        TotalPriceBean totalPriceBean = new TotalPriceBean(couponApplier.getFinalPrice().getPrice());
        if(dummyPaymentBoundary.pay(totalPriceBean)){   // se il pagamento va a buon fine
            Order newOrder = new Order(orderBean.getShopId(), customerId, addressBean.getShippingStreet(), addressBean.getShippingStreetNumber(),  addressBean.getShippingCity(), addressBean.getShippingProvince(), "pronto" );
            newOrder.setOrderTime(LocalDateTime.now());
            newOrder.setItems(cart.getItemList());
            Order savedOrder = orderDAO.saveOrder(newOrder);
            notificationDAO.insertNotificationRes(savedOrder, "nuovo ordine ricevuto!");
            Session.getSession().flushSessionCart();
        }
        else{
            throw new PaymentFailedException("pagamento non andato a buon fine");
        }
    }

    public List<NotificationBean> loadNotification() throws DAOException {

        NotificationDAO notificationDAO = new NotificationDAO(SingletonConnector.getInstance().getConnection());

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory(); // restituirà un 'istanza di DbDAOFactory o FileDAOFactory a seconda della scelta fatta nel factoryProvider
        ShopDAO shopDAO;
        try {
            shopDAO = daoAbsFactory.createShopDAO();
        } catch (ConfigurationException e) {
            throw new DAOException("Errore nella configurazione durante la creazione della ShopDAO: " + e.getMessage());
        }
        Shop shop = shopDAO.retrieveShopByEmail(Session.getSession().getUser().getEmail());

        List<Notification> notificationList;
        try {
            notificationList = notificationDAO.getNotificationsForOwner(shop.getVATnumber());
        } catch (DAOException e) {
            throw new DAOException(e.getMessage());
        }

        List<NotificationBean> notificationBeanList = new ArrayList<>();
        for(Notification notification : notificationList){

            Order notifiedOrder = notification.getOrder();
            AddressBean addressBean = new AddressBean(notifiedOrder.getShippingStreet(), notifiedOrder.getShippingStreetNumber(), notifiedOrder.getShippingCity(), notifiedOrder.getShippingProvince());
            OrderBean orderBean = new OrderBean(notifiedOrder.getCustomerId(), notifiedOrder.getOrderId(), addressBean);

            NotificationBean notificationBean = new NotificationBean(orderBean, notification.getMessageText());
            notificationBean.setNotificationId(notification.getNotificationId());
            notificationBeanList.add(notificationBean);
        }
        return notificationBeanList;
    }

    public void markNotificationAsRead(List<NotificationBean> notificationBeanList) throws DAOException {
        DAOFactory daoFactory = new DAOFactory();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();
        for(NotificationBean notificationBean: notificationBeanList){
            notificationDAO.markNotificationAsRead(notificationBean.getNotificationId());
        }
    }
}
