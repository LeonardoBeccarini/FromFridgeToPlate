package com.example.fromfridgetoplate.logic.control;

import com.example.fromfridgetoplate.logic.bean.RiderBean;
import com.example.fromfridgetoplate.logic.dao.NotificationDAO;
import com.example.fromfridgetoplate.logic.dao.RiderDAO;
import com.example.fromfridgetoplate.logic.model.Session;
import com.example.fromfridgetoplate.patterns.factory.DAOFactory;


public class SessionController {

    public RiderBean getRiderDetailsFromSession() {

        DAOFactory daoFactory = new DAOFactory();
        RiderDAO riderDAO = daoFactory.getRiderDAO();
        return riderDAO.getRiderDetailsFromSession();
    }

}
