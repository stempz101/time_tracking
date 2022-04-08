package com.tracking.controllers.servlets.admin.users;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.users.UsersService;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet, that responsible for showing Users page (admin)
 */
@WebServlet("/a/users")
public class UsersServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UsersServlet.class);

    UsersService usersService = null;

    @Override
    public void init() throws ServletException {
        usersService = new UsersService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getParameter("lang") != null)
                req.getSession().setAttribute("lang", req.getParameter("lang"));
            if (!usersService.processUsers(req, resp))
                return;
            usersService.setQueryStringForPagination(req);
            logger.info("Opening Users page (admin)");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/users/users.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
