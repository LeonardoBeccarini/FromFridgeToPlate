package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.boundary.DummyPaymentBoundary;
import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.logic.exceptions.*;
import com.example.fromfridgetoplate.logic.model.*;
import com.example.fromfridgetoplate.patterns.factory.CatalogDAOFactory;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MakeOrderControl {

    private final Catalog catalog;
    private final Cart cart = Session.getSession().getCart();
    private final CouponApplier couponApplier = new CouponApplier(cart);

    public MakeOrderControl(ShopBean shopBean) throws DbException, IOException, CatalogDAOFactoryError, EmptyCatalogException {
        Catalog catalog1;
        CatalogDAOFactory catalogDAOFactory = new CatalogDAOFactory();
        //provo a prendere il catalogo sia da file sia da db
        CatalogDAO catalogDAO = catalogDAOFactory.createCatalogDAO(PersistenceType.JDBC);
        CatalogDAO catalogDAO1 = catalogDAOFactory.createCatalogDAO(PersistenceType.FILE_SYSTEM);
        if((catalog1 = catalogDAO.retrieveCatalog(shopBean.getVatNumber())) != null) {
            this.catalog = catalog1;
        }
       else if((catalog1 = catalogDAO1.retrieveCatalog(shopBean.getVatNumber())) != null){
           this.catalog = catalog1;
        }
       else throw new EmptyCatalogException("il catologo del negozio selezionato è vuoto!");
    }

    public MakeOrderControl() {
        this.catalog = null;
    }

    public List<ShopBean> loadShop(SearchInfoBean searchInfoBean) throws DbException {
        List<ShopBean> listShopBean = new ArrayList<>();
        ShopDAO shopDAO = new ShopDAO();
        for(Shop shop: shopDAO.retrieveShopByName(searchInfoBean.getName())){
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


    public TotalPriceBean applyCoupon(CouponBean couponBean) throws CouponNotFoundException, DbException, NegativePriceException {
        CouponDAO couponDAO = new CouponDAO();
        Coupon retrievedCoupon =  couponDAO.retrieveCoupon(couponBean.getVatNumber(), couponBean.getCode());
        couponApplier.applyCoupon(retrievedCoupon);
        List<CouponBean> couponBeanList = convertToCouponBean(couponApplier.getCouponList());
        return  new TotalPriceBean(couponApplier.getFinalPrice().getPrice(), couponBeanList);

    }

    private List<CouponBean> convertToCouponBean(CouponList couponList){
        List<CouponBean> couponBeanList= new ArrayList<>();
        for (Coupon coupon : couponList.getCouponList()){
            CouponBean couponBean = new CouponBean(coupon.getCode());
            couponBeanList.add(couponBean);
        }
        return  couponBeanList;
    }

    // metodo per salvare l'ordine sul db e notificare lo shop owner, e per chiamare l'API del pagamento?
    public void completeOrder(OrderBean orderBean) throws DbException, PaymentFailedException {
        DAOFactory daoFactory = new DAOFactory();
        OrderDAO orderDAO = daoFactory.getOrderDAO();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();
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
        if(dummyPaymentBoundary.pay(totalPriceBean)){
            Order newOrder = new Order(orderBean.getShopId(), customerId, addressBean.getShippingStreet(), addressBean.getShippingStreetNumber(),  addressBean.getShippingCity(), addressBean.getShippingProvince() );
            newOrder.setItems(cart.getItemList());
            Order savedOrder = orderDAO.saveOrder(newOrder);
            notificationDAO.insertNotificationRes(savedOrder, "nuovo ordine ricevuto!");
        }
        else{
            throw new PaymentFailedException("pagamento non andato a buon fine");
        }



    }
    public List<NotificationBean> loadNotification() throws DbException {
        DAOFactory daoFactory = new DAOFactory();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();

        ShopDAO shopDAO = new ShopDAO();
        Shop shop = shopDAO.retrieveShopByEmail(Session.getSession().getUser().getEmail());

        List<Notification> notificationList = notificationDAO.getNotificationsForOwner(shop.getVATnumber());
        List<NotificationBean> notificationBeanList = new ArrayList<>();
        for(Notification notification : notificationList){
            NotificationBean notificationBean = new NotificationBean(notification.getCustomer(), notification.getOrderId(), notification.getStreet(),
                    notification.getStreetNumber(), notification.getCity(), notification.getProvince(), notification.getMessageText());
            notificationBean.setNotificationId(notification.getNotificationId());
            notificationBeanList.add(notificationBean);
        }
        return notificationBeanList;
    }
}
