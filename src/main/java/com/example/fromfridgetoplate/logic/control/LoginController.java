package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.dao.UserDAO;
import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;
import com.example.fromfridgetoplate.logic.model.Cart;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.logic.model.User;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOFactoryProvider;
import javafx.scene.control.Alert;

public class LoginController {
    /*chiama il dao che*/
    public UserBean login(UserBean userBean) throws NotExistentUserException{

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory(); // questa è responsabile di creare istanze di FileDAOFactory() o DbDAOFactory(), implementazioni concrete di DAOAbsFactory
        UserDAO userDAO = daoAbsFactory.createUserDAO();
        UserBean loggedUser;
        User user = null;
        try{
            user = userDAO.verifyUserCredentials(userBean.getEmail(), userBean.getPassword());
        }catch(NotExistentUserException e){
            throw new NotExistentUserException(e.getMessage());
        }
        assert user != null;
        loggedUser = new UserBean(user.getEmail(), user.getRole());

        if(user.getRole() == Role.CLIENT){
            Session.init(user);
            Session.getSession().setCart(new Cart());
        }
        else{Session.init(user); /*setta il session user (dovrei metterci i figli, cioè client owner e rider?)*/}
        return  loggedUser;
    }

}
