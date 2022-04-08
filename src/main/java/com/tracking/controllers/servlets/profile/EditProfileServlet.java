package com.tracking.controllers.servlets.profile;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.profile.ProfileService;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet, that responsible for showing Edit Profile page and updating action (admin/user)
 */
@WebServlet(urlPatterns = {"/a/edit-prof", "/u/edit-prof"})
public class EditProfileServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EditProfileServlet.class);

    ProfileService profileService = null;

    @Override
    public void init() throws ServletException {
        profileService = new ProfileService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("lang") != null)
            req.getSession().setAttribute("lang", req.getParameter("lang"));
        logger.info("Opening Edit Profile page");
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/profile/editProfile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lastName = req.getParameter("last_name").strip();
        String firstName = req.getParameter("first_name").strip();
        String email = req.getParameter("email").strip();

        try {
            User authUser = (User) req.getSession().getAttribute("authUser");
            String role = authUser.isAdmin() ? "/a" : "/u";
            if (profileService.editProfile(req, authUser.getId(), lastName, firstName, email)) {
                logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                        role + "/profile?id=" + authUser.getId()));
                resp.sendRedirect(req.getContextPath() + role + "/profile?id=" + authUser.getId());
                return;
            }
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    role + "/edit-prof"));
            resp.sendRedirect(req.getContextPath() + role + "/edit-prof");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
