package com.tracking.services;

import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class LoginService {
    public User authUser(HttpSession session, String email, String password) {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        User user = null;
        try {
            user = userDAO.auth(session, email, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
