package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.dao.CatalogDAO;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Catalog;
import com.example.fromfridgetoplate.logic.model.FoodItem;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.logic.model.Shop;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;

import java.io.IOException;

public class ManageCatalogController {

    public FoodItemListBean getIngredients() throws DbException, IOException {
        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        CatalogDAO catalogDAO = daoAbsFactory.createCatalogDAO();
        ShopDAO shopDAO = daoAbsFactory.createShopDAO();

       FoodItemListBean foodList = new FoodItemListBean();
        Shop shop = shopDAO.retrieveShopByEmail(Session.getSession().getUser().getEmail());
        Catalog catalog = catalogDAO.retrieveCatalog(shop.getVATnumber());
        for(FoodItem foodItem: catalog.getItems())
            foodList.addFoodItem(new FoodItemBean(foodItem.getName(), foodItem.getPrice()));
        return foodList;
    }

    public void addIngredient(FoodItemBean foodItemBean) throws DbException, IOException{
        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        CatalogDAO catalogDAO = daoAbsFactory.createCatalogDAO();
        ShopDAO shopDAO = daoAbsFactory.createShopDAO();

        Shop shop = shopDAO.retrieveShopByEmail(Session.getSession().getUser().getEmail());
        catalogDAO.addItem(foodItemBean.getName(), foodItemBean.getPrice(), shop.getVATnumber());
    }

}
