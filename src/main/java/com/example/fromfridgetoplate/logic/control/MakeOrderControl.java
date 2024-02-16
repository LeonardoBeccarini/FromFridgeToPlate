package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.dao.*;
import com.example.fromfridgetoplate.logic.exceptions.CouponNotFoundException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.exceptions.NegativePriceException;
import com.example.fromfridgetoplate.logic.model.*;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MakeOrderControl {

    private final Catalog catalog;
    private final Cart cart = Session.getSession().getCart();
    private final CouponApplier couponApplier = new CouponApplier(cart);

    public MakeOrderControl(ShopBean shopBean){
        CatalogDAO catalogDAO = new CatalogDAO();
        this.catalog = catalogDAO.retrieveCatalog(shopBean.getVatNumber());
    }

    public MakeOrderControl() {
        this.catalog = null;
    }

    public List<ShopBean> loadShop(SearchInfoBean searchInfoBean){
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
    public void completeOrder(OrderBean orderBean) throws DbException {
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

        Order newOrder = new Order(orderBean.getShopId(), customerId, addressBean.getShippingStreet(), addressBean.getShippingStreetNumber(),  addressBean.getShippingCity(), addressBean.getShippingProvince() );
        newOrder.setItems(cart.getItemList());
        Order savedOrder = orderDAO.saveOrder(newOrder);

        notificationDAO.insertNotificationBecca(savedOrder, "nuovo ordine ricevuto!");

    }
    public List<NotificationBean> loadNotification(){
        DAOFactory daoFactory = new DAOFactory();
        NotificationDAO notificationDAO = daoFactory.getNotificationDAO();

        ShopDAO shopDAO = new ShopDAO();
        Shop shop = shopDAO.retrieveShopByEmail(Session.getSession().getUser().getEmail());

        List<Notification> notificationList = notificationDAO.getNotificationsForOwner(shop.getVATnumber());
        List<NotificationBean> notificationBeanList = new ArrayList<>();
        for(Notification notification : notificationList){
            notificationBeanList.add(new NotificationBean(notification.getCustomer(), notification.getOrderId(), notification.getStreet(),
                    notification.getStreetNumber(), notification.getCity(), notification.getProvince(), notification.getMessageText()));



        }
        return notificationBeanList;
    }
}
