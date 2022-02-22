package com.tracking.controllers.servlets.admin.users;

import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/a/user-set-block")
public class SetBlockServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("id"));
        boolean isBlocked = Boolean.parseBoolean(req.getParameter("value"));
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();

        try {
            HttpSession session = req.getSession();
            if (isBlocked) {
                userDAO.setBlock(userId, true);
                session.setAttribute("successMessage", "");
            } else {
                userDAO.setBlock(userId, false);
                session.setAttribute("successMessage", "");
            }
            resp.sendRedirect(req.getContextPath() + "/a/users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
