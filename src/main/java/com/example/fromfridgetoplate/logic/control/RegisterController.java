package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.RegistrationBean;


import com.example.fromfridgetoplate.logic.dao.ClientDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.dao.DbShopDAO;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Client;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.Shop;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.factory.FileDAOFactory;
import com.example.fromfridgetoplate.patterns.factory.UserFactory;


public class RegisterController {
    public boolean register(RegistrationBean registrationBean) throws DbException {

        UserFactory userFactory = new UserFactory();
        Role role = registrationBean.getRole();
        DAOAbsFactory daoAbsFactory = new FileDAOFactory();

        if(role == Role.OWNER){
            //DbShopDAO shopDAO = new DbShopDAO();
            Shop newShop = (Shop) userFactory.createUser(registrationBean);
            ShopDAO shopDAO = daoAbsFactory.createShopDAO();
            return shopDAO.saveShop(newShop);


        }
        else if(role == Role.CLIENT){
            ClientDAO clientDAO = new ClientDAO();
            Client newClient = (Client) userFactory.createUser(registrationBean);
            return clientDAO.saveClient(newClient);

        }

        return false;
    }


    public boolean registerRider(RegistrationBean registrationBean) {
        UserFactory userFactory = new UserFactory();
        //DbRiderDAO riderDAO = new DAOFactory().getRiderDAO();
        Rider newRider = (Rider) userFactory.createUser(registrationBean); // casto il padre

        DAOAbsFactory daoAbsFactory = new FileDAOFactory(); // cambiare qui per lo switch
        RiderDAO riderDAO = daoAbsFactory.createRiderDAO();

        System.out.println("register rider ok");
        return riderDAO.registerRider(newRider);

    }



}