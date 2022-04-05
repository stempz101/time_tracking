package com.tracking.controllers.servlets;

import com.tracking.controllers.services.Service;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet, that responsible for logout action (admin/user)
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        logger.info("Removing values from session");
        session.removeAttribute("authUser");
        logger.info("Redirecting to " + Service.getFullURL(req, "/login"));
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
