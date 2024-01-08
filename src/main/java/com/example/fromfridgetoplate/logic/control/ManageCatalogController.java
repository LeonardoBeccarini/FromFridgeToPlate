package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.FoodItemBean;
import com.example.fromfridgetoplate.logic.dao.CatalogDAO;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.model.Catalog;
import com.example.fromfridgetoplate.logic.model.FoodItem;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.logic.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class ManageCatalogController {
    public List<FoodItemBean> getIngredients(){
        CatalogDAO catalogDAO = new CatalogDAO();
        ShopDAO shopDAO = new ShopDAO();
        List<FoodItemBean> ingredientsList = new ArrayList<>() {
        };
        Shop shop = shopDAO.retrieveShopByEmail(Session.getSession().getUser().getEmail());
        Catalog catalog = catalogDAO.retrieveCatalog(shop.getName());
        for(FoodItem foodItem: catalog.getItems())
            ingredientsList.add(new FoodItemBean(foodItem.getName(), foodItem.getPrice()));
        return ingredientsList;
    }

}
