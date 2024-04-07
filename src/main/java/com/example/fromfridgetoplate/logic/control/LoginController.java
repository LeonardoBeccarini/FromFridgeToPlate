package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.dao.DbUserDAO;
import com.example.fromfridgetoplate.logic.dao.UserDAO;
import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;
import com.example.fromfridgetoplate.logic.model.Cart;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.logic.model.User;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstractFactory.DAOFactoryProvider;
import com.example.fromfridgetoplate.patterns.factory.FileDAOFactory;
import javafx.scene.control.Alert;

public class LoginController {
    /*chiama il dao che*/
    public static UserBean login(UserBean userBean){

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory(); // questa è responsabile di creare istanze di FileDAOFactory() o DbDAOFactory(), implementazioni concrete di DAOAbsFactory
        UserDAO userDAO = daoAbsFactory.createUserDAO();
        UserBean loggedUser;
        User user = null;
        try{
            System.out.println("check login");
            user = userDAO.verifyUserCredentials(userBean.getEmail(), userBean.getPassword());
        }catch(NotExistentUserException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage()) ;
            alert.showAndWait();
        }
        assert user != null;
        loggedUser = new UserBean(user.getEmail(), user.getRole());

        if(user.getRole() == Role.CLIENT){
            Session.init(user);
            Session.getSession().setCart(new Cart());
        }
        else{ Session.init(user); /*setta il session user (dovrei metterci i figli, cioè client owner e rider?)*/}
        return loggedUser;
    }

    public static void main(String[] args) {
        // Crea un UserBean con credenziali fittizie
        UserBean userBeanToLogin = new UserBean("marco4@gmail.com", "marco4");

        // Esegui il login
        UserBean loggedInUser = login(userBeanToLogin);

        // Controlla se l'utente è stato effettivamente autenticato e stampa i dettagli
        if (loggedInUser != null) {
            System.out.println("Login riuscito per l'utente con email: " + loggedInUser.getEmail() + " e ruolo: " + loggedInUser.getRole());
        } else {
            System.out.println("Login fallito.");
        }
    }
}
