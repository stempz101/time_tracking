package com.tracking.controllers.servlets.profile;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.profile.ProfileService;
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
 * Servlet, that responsible for showing Profile page (admin/user)
 */
@WebServlet(urlPatterns = {"/a/profile", "/u/profile"})
public class ProfileServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ProfileServlet.class);

    ProfileService profileService = null;

    @Override
    public void init() throws ServletException {
        profileService = new ProfileService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getParameter("lang") != null)
                req.getSession().setAttribute("lang", req.getParameter("lang"));
            if (!profileService.processProfile(req, resp))
                return;
            profileService.setQueryStringForPagination(req);
            logger.info("Opening Profile page");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/profile/profile.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
