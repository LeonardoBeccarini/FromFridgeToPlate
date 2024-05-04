package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.ShopBean;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Shop;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;

public class ShopProfileController {


    public ShopBean getShopByEmail(ShopBean shopBean) throws DbException {

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();
        ShopDAO shopDAO = null;
        try {
            shopDAO = daoAbsFactory.createShopDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DbException("Errore nella configurazione durante la creazione della RiderDAO: " + e.getMessage());
        }

        Shop shop = shopDAO.retrieveShopByEmail(shopBean.getEmail());
        return new ShopBean(shop.getName(), shop.getAddress(), shop.getPhoneNumber(), shop.getVATnumber());
    }



}
