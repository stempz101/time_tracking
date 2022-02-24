package com.tracking.services;

import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.User;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.sql.SQLException;

public class RegisterService extends Service {
    public User registerUser(HttpSession session, String lastName, String firstName, String email,
                             String password, String confirmPassword, String image) {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        User user = null;
        try {
            user = userDAO.create(session, lastName, firstName, email, password, confirmPassword, image);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
