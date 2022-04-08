package com.tracking.controllers.servlets.profile;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.profile.ProfileService;
import com.tracking.controllers.servlets.admin.users.UsersServlet;
import com.tracking.models.User;
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
 * Servlet, that responsible for showing Edit Password page and updating action (admin/user)
 */
@WebServlet(urlPatterns = {"/a/edit-pass", "/u/edit-pass"})
public class EditPasswordServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EditPasswordServlet.class);

    ProfileService profileService = null;

    @Override
    public void init() throws ServletException {
        profileService = new ProfileService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("lang") != null)
            req.getSession().setAttribute("lang", req.getParameter("lang"));
        logger.info("Opening Edit Password page");
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/profile/editPassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        try {
            User authUser = (User) req.getSession().getAttribute("authUser");
            String role = authUser.isAdmin() ? "/a" : "/u";
            if (profileService.editPassword(req, authUser.getId(), currentPassword, newPassword, confirmPassword)) {
                logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                        role + "/profile?id=" + authUser.getId()));
                resp.sendRedirect(req.getContextPath() + role + "/profile?id=" + authUser.getId());
                return;
            }
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    role + "/edit-pass"));
            resp.sendRedirect(req.getContextPath() + role + "/edit-pass");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
