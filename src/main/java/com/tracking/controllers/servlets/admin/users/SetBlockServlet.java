package com.tracking.controllers.servlets.admin.users;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.users.UsersService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Servlet, that responsible for blocking/unblocking user (admin)
 */
@WebServlet("/a/user-set-block")
public class SetBlockServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(SetBlockServlet.class);

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
        try {
            HttpSession session = req.getSession();
            usersService.setBlock(userId, isBlocked);
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            if (isBlocked)
                session.setAttribute("successMessage", bundle.getString("message.user_blocked"));
            else
                session.setAttribute("successMessage", bundle.getString("message.user_unblocked"));
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    "/a/users"));
            resp.sendRedirect(req.getContextPath() + "/a/users");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
