package com.tracking.controllers.servlets.admin.users;

import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.services.users.UsersService;

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

    UsersService usersService = null;

    @Override
    public void init() throws ServletException {
        usersService = new UsersService();
    }

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
            usersService.setBlock(userId, isBlocked);
            if (isBlocked)
                session.setAttribute("successMessage", "");
            else
                session.setAttribute("successMessage", "");
            resp.sendRedirect(req.getContextPath() + "/a/users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}