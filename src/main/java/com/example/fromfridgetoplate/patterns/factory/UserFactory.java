package com.example.fromfridgetoplate.patterns.factory;

import com.example.fromfridgetoplate.logic.bean.RegistrationBean;
import com.example.fromfridgetoplate.logic.model.*;


public class UserFactory {
    public User createUser(RegistrationBean registrationBean){  /* ha senso passare una bean alla factory??? se non lo sai te*/
        if(registrationBean.getRole() == Role.CLIENT){
            return new Client(registrationBean.getEmail(), registrationBean.getPassword(), registrationBean.getName(), registrationBean.getSurname(), registrationBean.getAddress());
        }
        else if(registrationBean.getRole() == Role.OWNER){
            return new Shop(registrationBean.getEmail(), registrationBean.getPassword(), registrationBean.getName(), registrationBean.getAddress(), registrationBean.getVATnumber(), registrationBean.getPhoneNumber());    /* qui andrebbe if il ruolo è rider o owner creo le altre due entity, questo è un placeholder per evitare errori del compilatore*/
        }
        //return new Rider(registrationBean.getEmail(), registrationBean.getPassword(), registrationBean.getName(), registrationBean.getSurname());
        return null;
    }
}
