package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.dao.UserDAO;
import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;
import com.example.fromfridgetoplate.logic.model.Cart;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.logic.model.User;
import javafx.scene.control.Alert;

public class LoginController {
    /*chiama il dao che*/
    public UserBean login(UserBean userBean){
        UserDAO userDAO = new UserDAO();
        UserBean loggedUser;
        User user = null;
        try{
            user = userDAO.verifyUserCredentials(userBean.getEmail(), userBean.getPassword());
        }catch(NotExistentUserException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage()) ;
            alert.showAndWait();
        }
        loggedUser = new UserBean(user.getEmail(), user.getRole());

        Session.init(user, new Cart()); /*setta il session user (dovrei metterci i figli, cioè client owner e rider??)*/
        return loggedUser;
    }
}
