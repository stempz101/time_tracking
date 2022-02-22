package com.tracking.services.users;

import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.User;
import com.tracking.services.Service;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;

public class UsersService extends Service {

    public List<User> getAll(int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<User> userList = null;
        try {
            userList = userDAO.getAll(start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    public List<User> getAllSorted(String sort, String order, int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<User> userList = null;
        try {
            userList = userDAO.getAllOrder(sort, order, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    public List<User> getAllWhereName(String lastName, String firstName, int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<User> userList = null;
        try {
            userList = userDAO.getAllWhereName(lastName, firstName, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    public List<User> getAllWhereNameSorted(String lastName, String firstName, String sort, String order,
                                            int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<User> userList = null;
        try {
            userList = userDAO.getAllWhereNameOrder(lastName, firstName, sort, order, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    public int getCount() throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            return userDAO.getCount();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public int getCountWhereName(String lastName, String firstName) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            return userDAO.getCountWhereName(lastName, firstName);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public boolean processUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        try {
            List<User> userList;

            int start = 1;
            int total = 10;
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0)
                    return false;
                start = start + total * (page - 1);
            }

            int userCount = getCount();
            if (req.getParameter("lastName") != null || req.getParameter("firstName") != null) {
                String lastName = req.getParameter("lastName");
                String firstName = req.getParameter("firstName");
                req.setAttribute("lastName", lastName);
                req.setAttribute("firstName", firstName);
                userCount = getCountWhereName(lastName, firstName);
                if (req.getParameter("sortBy") != null && !req.getParameter("sortBy").isEmpty()) {
                    String sort = req.getParameter("sortBy");
                    String order = req.getParameter("order");
                    if (order == null || order.isEmpty())
                        order = "asc";
                    userList = getAllWhereNameSorted(lastName, firstName, sort, order, start, total);
                } else {
                    userList = getAllWhereName(lastName, firstName, start, total);
                }
            } else if (req.getParameter("sortBy") != null && !req.getParameter("sortBy").isEmpty()) {
                String sort = req.getParameter("sortBy");
                String order = req.getParameter("order");
                if (order == null || order.isEmpty())
                    order = "asc";
                userList = getAllSorted(sort, order, start, total);
            } else {
                userList = getAll(start, total);
            }
            int pageCount = userCount % total == 0 ? userCount / total
                    : userCount / total + 1;
            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (userCount > total && page < pageCount)
                nextPage = page + 1;

            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("userList", userList);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return true;
    }

    public void setAdmin(int userId, boolean admin) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            userDAO.setAdmin(userId, admin);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void setBlock(int userId, boolean blocked) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            userDAO.setBlock(userId, blocked);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }
}
