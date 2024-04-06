package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.bean.FoodItemListBean;
import com.example.fromfridgetoplate.logic.dao.CatalogDAO;
import com.example.fromfridgetoplate.logic.dao.PersistenceType;
import com.example.fromfridgetoplate.logic.dao.DbShopDAO;
import com.example.fromfridgetoplate.logic.exceptions.CatalogDAOFactoryError;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Catalog;
import com.example.fromfridgetoplate.logic.model.FoodItem;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.logic.model.Shop;
import com.example.fromfridgetoplate.patterns.factory.CatalogDAOFactory;

import java.io.IOException;

public class ManageCatalogController {
    private PersistenceType persistenceType;

    public ManageCatalogController(PersistenceType persistenceType) {
        this.persistenceType = persistenceType;
    }

    public FoodItemListBean getIngredients() throws DbException, IOException, CatalogDAOFactoryError {
        CatalogDAOFactory catalogDAOFactory = new CatalogDAOFactory();
        CatalogDAO catalogDAO = catalogDAOFactory.createCatalogDAO(persistenceType);
        DbShopDAO shopDAO = new DbShopDAO();
       FoodItemListBean foodList = new FoodItemListBean();
        Shop shop = shopDAO.retrieveShopByEmail(Session.getSession().getUser().getEmail());
        Catalog catalog = catalogDAO.retrieveCatalog(shop.getVATnumber());
        for(FoodItem foodItem: catalog.getItems())
            foodList.addFoodItem(new FoodItemBean(foodItem.getName(), foodItem.getPrice()));
        return foodList;
    }

    public void addIngredient(FoodItemBean foodItemBean) throws DbException, IOException, CatalogDAOFactoryError {
        CatalogDAOFactory catalogDAOFactory = new CatalogDAOFactory();
        CatalogDAO catalogDAO = catalogDAOFactory.createCatalogDAO(persistenceType);
        DbShopDAO shopDAO = new DbShopDAO();
        Shop shop = shopDAO.retrieveShopByEmail(Session.getSession().getUser().getEmail());
        catalogDAO.addItem(foodItemBean.getName(), foodItemBean.getPrice(), shop.getVATnumber());
    }

}
