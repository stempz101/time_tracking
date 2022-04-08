package com.tracking.controllers.services;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.servlets.LoginServlet;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Service, that responsible for Login action
 */
public class LoginService extends Service {

    private static final Logger logger = Logger.getLogger(LoginService.class);

    /**
     * Identification and authorization of user
     * @param req for getting session to get/set attributes, getting parameters
     * @param email entered email
     * @param password entered password
     * @return authorized user
     * @throws ServiceException if something went wrong while executing
     */
    public User authUser(HttpServletRequest req, String email, String password) throws ServiceException {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            if (!UserDAO.validateEmail(email)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.email_or_password_error"));
                logger.error("Validation error occurred (email, password): " + req.getSession().getAttribute("messageError"));
                return null;
            }

            DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
            UserDAO userDAO = factory.getUserDao();

            if (!userDAO.checkEmail(email)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.email_or_password_error"));
                logger.error("Error in checking email: " + req.getSession().getAttribute("messageError"));
                return null;
            }
            User dbUser = userDAO.getByEmail(email);
            if (!userDAO.checkPassword(dbUser, password)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.email_or_password_error"));
                logger.error("Error in checking password: " + req.getSession().getAttribute("messageError"));
                return null;
            }
            return dbUser;
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("LoginService: authUser was failed", e);
        }
    }
}
