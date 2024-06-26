package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.UserBean;
import com.example.fromfridgetoplate.logic.dao.UserDAO;
import com.example.fromfridgetoplate.logic.exceptions.ConfigurationException;
import com.example.fromfridgetoplate.logic.exceptions.DAOException;
import com.example.fromfridgetoplate.logic.exceptions.NotExistentUserException;
import com.example.fromfridgetoplate.logic.model.Cart;
import com.example.fromfridgetoplate.logic.model.Role;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.logic.model.User;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOAbsFactory;
import com.example.fromfridgetoplate.patterns.abstract_factory.DAOFactoryProvider;

public class LoginController {
    public UserBean login(UserBean userBean) throws NotExistentUserException, DAOException{

        DAOAbsFactory daoAbsFactory = DAOFactoryProvider.getInstance().getDaoFactory(); // questa è responsabile di creare istanze di FileDAOFactory() o DbDAOFactory(), implementazioni concrete di DAOAbsFactory
        UserDAO userDAO = null;
        try {
            userDAO = daoAbsFactory.createUserDAO();
        } catch (ConfigurationException e) {
            // passo il messaggio e la causa originale dall'eccezione ConfigurationException alla nuova
            // DAOException per non perdere il contesto dell'errore originale

            throw new DAOException("Errore nella configurazione durante la creazione della UserDAO: " + e.getMessage(), e);
        }
        UserBean loggedUser;
        User user;
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
        else{Session.init(user);}
        return  loggedUser;
    }

}
