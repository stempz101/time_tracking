package com.tracking.controllers.services;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Service, that responsible for registration actions
 */
public class RegisterService extends Service {

    private static final Logger logger = Logger.getLogger(RegisterService.class);

    /**
     * Registration of User
     * @param req for getting session to get/set attributes, getting parameters
     * @param lastName entered last name
     * @param firstName entered first name
     * @param email entered email
     * @param password entered password
     * @param confirmPassword entered confirmation of password
     * @param image chosen image
     * @return registered User
     * @throws ServiceException if something went wrong while executing
     */
    public User registerUser(HttpServletRequest req, String lastName, String firstName, String email,
                             String password, String confirmPassword, String image) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        saveFields(req, lastName, firstName, email);
        User user;
        try {
            user = userDAO.create(req, lastName, firstName, email, password, confirmPassword, image, UserDAO.IS_USER);
            if (user != null)
                deleteAttributes(req);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RegisterService: registerUser was failed", e);
        }
        return user;
    }

    /**
     * Registration of Admin
     * @param req for getting session to get/set attributes, getting parameters
     * @param lastName entered last name
     * @param firstName entered first name
     * @param email entered email
     * @param password entered password
     * @param confirmPassword entered confirmation of password
     * @param image chosen image
     * @return registered Admin
     * @throws ServiceException if something went wrong while executing
     */
    public User registerAdmin(HttpServletRequest req, String lastName, String firstName, String email,
                              String password, String confirmPassword, String image) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        saveFields(req, lastName, firstName, email);
        User user;
        try {
            user = userDAO.create(req, lastName, firstName, email, password, confirmPassword, image, UserDAO.IS_ADMIN);
            if (user != null)
                deleteAttributes(req);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("RegisterService: registerAdmin was failed", e);
        }
        return user;
    }

    /**
     * Saving entered fields (last name, first name, email) to session
     * @param req for getting session to get/set attributes, getting parameters
     * @param lastName entered last name
     * @param firstName entered first name
     * @param email entered email
     */
    private void saveFields(HttpServletRequest req, String lastName, String firstName, String email) {
        HttpSession session = req.getSession();
        session.setAttribute("lastName", lastName);
        session.setAttribute("firstName", firstName);
        session.setAttribute("email", email);
    }

    /**
     * Deleting saved fields (last name, first name, email) from session
     * @param req for getting session to get/set attributes, getting parameters
     */
    private void deleteAttributes(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.removeAttribute("lastName");
        session.removeAttribute("firstName");
        session.removeAttribute("email");
    }
}
