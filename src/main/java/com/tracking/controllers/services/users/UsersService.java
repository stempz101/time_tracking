package com.tracking.controllers.services.users;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.requests.RequestsService;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Service, that contains all actions with users
 */
public class UsersService extends Service {

    private static final Logger logger = Logger.getLogger(UsersService.class);

    /**
     * Getting all essential for Users page
     * @param req for getting session to get/set attributes, getting parameters
     * @param resp for sending redirect
     * @return true if process of categories was successful, false if request parameter "page" equal or less than 0 and
     * greater than assigned page count
     * @throws IOException if something went wrong while redirecting
     * @throws ServiceException if something went wrong while executing
     */
    public boolean processUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<User> userList;
        try {
            User authUser = (User) req.getSession().getAttribute("authUser");

            int start = 1;
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0) {
                    redirectWithoutPage(req, resp, "users", authUser);
                    return false;
                }
                start = start + TOTAL_USERS * (page - 1);
            }

            int userCount;
            String sort = req.getParameter("sortBy");
            String order = req.getParameter("order");
            if (order == null || order.isEmpty())
                order = "asc";
            if (req.getParameter("lastName") != null || req.getParameter("firstName") != null) {
                String lastName = req.getParameter("lastName");
                String firstName = req.getParameter("firstName");
                req.setAttribute("lastName", lastName);
                req.setAttribute("firstName", firstName);

                userCount = userDAO.getCountWhereName(lastName, firstName);
                userList = userDAO.getAllWhereName(lastName, firstName, sort, order, start, TOTAL_USERS);
            } else {
                userCount = userDAO.getCount();
                userList = userDAO.getAll(sort, order, start, TOTAL_USERS);
            }

            int pageCount = getPageCount(userCount, TOTAL_USERS);
            if (page > pageCount) {
                redirectWithoutPage(req, resp, "users", authUser);
                return false;
            }

            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (userCount > TOTAL_USERS && page < pageCount)
                nextPage = page + 1;

            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("userList", userList);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("UsersService: processUsers was failed", e);
        }
        return true;
    }

    /**
     * Setting blocked status to user
     * @param userId id of user
     * @param blocked true - block, false - unblock
     * @throws ServiceException if something went wrong while executing
     */
    public void setBlock(int userId, boolean blocked) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            userDAO.setBlock(userId, blocked);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("UsersService: setBlock was failed", e);
        }
    }
}
