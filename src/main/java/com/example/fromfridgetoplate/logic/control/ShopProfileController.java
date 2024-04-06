package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.dao.DbShopDAO;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Shop;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;
import com.example.fromfridgetoplate.patterns.factory.FileDAOFactory;

public class ShopProfileController {


    public ShopBean getShopByEmail(ShopBean shopBean) throws DbException {

        DAOAbsFactory daoAbsFactory = new FileDAOFactory();
        ShopDAO shopDAO = daoAbsFactory.createShopDAO();

        Shop shop = shopDAO.retrieveShopByEmail(shopBean.getEmail());
        return new ShopBean(shop.getName(), shop.getAddress(), shop.getPhoneNumber(), shop.getVATnumber());
    }



}
