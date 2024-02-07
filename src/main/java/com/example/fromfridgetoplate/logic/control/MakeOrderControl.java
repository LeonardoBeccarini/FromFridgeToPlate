package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.*;
import com.example.fromfridgetoplate.logic.dao.CatalogDAO;
import com.example.fromfridgetoplate.logic.dao.CouponDAO;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.exceptions.CouponNotFoundException;
import com.example.fromfridgetoplate.logic.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MakeOrderControl {

    private final Catalog catalog;
    private final Cart cart = Session.getSession().getCart();

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
        Cart cart =  Session.getSession().getCart();
        ArrayList<CartItemBean> itemListBean = new ArrayList<>();
        for(CartItem item: cart.getItemList()){
            itemListBean.add(new CartItemBean(item.getName(), item.getPrice(), item.getQuantity()));
        }
        return new CartBean(itemListBean);
    }

    public CouponBean verifyCoupon(CouponBean couponBean) throws CouponNotFoundException {
        CouponDAO couponDAO = new CouponDAO();
        try {
            Coupon coupon = couponDAO.retrieveCoupon(couponBean.getVatNumber(), couponBean.getCode());
            return new CouponBean(coupon.getCode());
        } catch (CouponNotFoundException e) {
            throw new CouponNotFoundException(e.getMessage());
        }

    }
}
