package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.model.Shop;

public class ShopProfileController {

    public ShopBean getShopByEmail(ShopBean shopBean){
        ShopDAO shopDAO = new ShopDAO(); // qui dovrei usare la dao factory
        Shop shop = shopDAO.retrieveShopByEmail(shopBean.getEmail());
        return new ShopBean(shop.getName(), shop.getAddress(), shop.getPhoneNumber(), shop.getVATnumber());
    }
}
