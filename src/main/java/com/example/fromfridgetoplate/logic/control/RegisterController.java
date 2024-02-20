package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.RegistrationBean;

import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.dao.ClientDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.dao.ShopDAO;
import com.example.fromfridgetoplate.logic.model.Client;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.Shop;
import com.example.fromfridgetoplate.patterns.factory.UserFactory;


public class RegisterController {
    public boolean register(RegistrationBean registrationBean){
        UserFactory userFactory = new UserFactory();
        Role role = registrationBean.getRole();
        if(role == Role.OWNER){
            ShopDAO shopDAO = new ShopDAO();
            Shop newShop = (Shop) userFactory.createUser(registrationBean);
            return shopDAO.saveShop(newShop.getEmail(), newShop.getPassword(), newShop.getName(), newShop.getVATnumber(), newShop.getAddress(), newShop.getPhoneNumber());

        }
        else if(role == Role.CLIENT){
            ClientDAO clientDAO = new ClientDAO();
            Client newClient = (Client) userFactory.createUser(registrationBean);
            return clientDAO.saveClient(newClient.getEmail(), newClient.getPassword(), newClient.getName(), newClient.getSurname(), newClient.getAddress());

        }
        RiderDAO riderDAO = new RiderDAO() // non esiste la stored procedure per la registrazione del rider
    }
}
