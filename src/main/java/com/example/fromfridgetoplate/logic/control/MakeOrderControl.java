package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.SearchInfoBean;
import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class MakeOrderControl {
    public List<ShopBean> loadShop(SearchInfoBean searchInfoBean){
        List<ShopBean> listShopBean = new ArrayList<>();
        ShopDAO shopDAO = new ShopDAO();
        for(Shop shop: shopDAO.retrieveShopByName(searchInfoBean.getName())){
            ShopBean shopBean = new
        }

        return listShopBean;
    }
}
