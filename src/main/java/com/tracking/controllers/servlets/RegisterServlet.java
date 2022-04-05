package com.tracking.controllers.servlets;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.models.User;
import com.tracking.controllers.services.RegisterService;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet, that responsible for showing Registration page and register action (unauthorized)
 */
@WebServlet("/register")
@MultipartConfig
public class RegisterServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegisterServlet.class);

    private RegisterService registerService = null;

    @Override
    public void init() throws ServletException {
        registerService = new RegisterService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Opening Registration page");
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lastName = req.getParameter("last_name");
        String firstName = req.getParameter("first_name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        Part image = req.getPart("image");
        String imageName = registerService.setImageName(image);

        try {
            User user = registerService.registerUser(req, lastName, firstName, email, password, confirmPassword, imageName);
            if (user == null) {
                logger.info("Redirecting to " + Service.getFullURL(req, "/register"));
                resp.sendRedirect(req.getContextPath() + "/register");
                return;
            }

            logger.info("User (id=" + user.getId() + ") registration were successful");
            registerService.saveUserImage(image, user.getImage(), getServletContext().getRealPath(""));
            logger.info("Authorized user (id=" + user.getId() + ", isAdmin=" + user.isAdmin() +  "): " +
                            user.getLastName() + " " + user.getFirstName());
            req.getSession().setAttribute("authUser", user);
            logger.info("Redirecting to " + Service.getFullURL(req, "/register"));
            resp.sendRedirect(req.getContextPath() + "/u/activities");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
