package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.RegistrationBean;
import com.example.fromfridgetoplate.logic.dao.ClientDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.DbException;
import com.example.fromfridgetoplate.logic.model.Client;
import com.example.fromfridgetoplate.logic.model.Rider;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.Shop;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;
import com.example.fromfridgetoplate.patterns.factory.UserFactory;


public class RegisterController {
    public boolean register(RegistrationBean registrationBean) throws DbException, DAOException {

        UserFactory userFactory = new UserFactory();
        Role role = registrationBean.getRole();
        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory();


        if(role == Role.OWNER){
            Shop newShop = (Shop) userFactory.createUser(registrationBean);
            ShopDAO shopDAO = daoAbsFactory.createShopDAO();
            return shopDAO.saveShop(newShop);


        }

        else if(role == Role.CLIENT){
            Client newClient = (Client) userFactory.createUser(registrationBean);
            ClientDAO clientDAO = daoAbsFactory.createClientDAO();
            return clientDAO.saveClient(newClient);

        }

        else if(role == Role.RIDER){
            Rider newRider = (Rider) userFactory.createUser(registrationBean); // casto il padre
            RiderDAO riderDAO = daoAbsFactory.createRiderDAO();
            return riderDAO.registerRider(newRider);

        }
        return false;
    }






}