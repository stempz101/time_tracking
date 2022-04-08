package com.tracking.controllers.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.models.User;
import com.tracking.controllers.services.LoginService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet, that responsible for showing Login page and identification and authorization action (unauthorized)
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class);

    private LoginService loginService = null;

    @Override
    public void init() throws ServletException {
        loginService = new LoginService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("lang") != null)
            req.getSession().setAttribute("lang", req.getParameter("lang"));
        logger.info("Opening Login page");
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = loginService.authUser(req, email, password);
            if (user == null) {
                logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                        "/login"));
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            deleteAttributes(req);
            req.getSession().setAttribute("authUser", user);
            if (user.isAdmin()) {
                logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                        "/a/activities"));
                resp.sendRedirect(req.getContextPath() + "/a/activities");
            }
            else {
                logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                        "/u/activities"));
                resp.sendRedirect(req.getContextPath() + "/u/activities");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    private void deleteAttributes(HttpServletRequest req) {
        HttpSession session = req.getSession();
        logger.info("Removing of session attributes (email, messageError) has started");
        session.removeAttribute("email");
        session.removeAttribute("messageError");
    }
}
