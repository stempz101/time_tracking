package com.tracking.controllers.servlets.profile;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.profile.ProfileService;
import com.tracking.models.User;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Servlet, that responsible for showing Edit Photo page and updating action (admin/user)
 */
@WebServlet(urlPatterns = {"/a/edit-photo", "/u/edit-photo"})
@MultipartConfig
public class EditPhotoServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EditPhotoServlet.class);

    ProfileService profileService = null;

    @Override
    public void init() throws ServletException {
        profileService = new ProfileService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("lang") != null)
            req.getSession().setAttribute("lang", req.getParameter("lang"));
        logger.info("Opening Edit Photo page (profile)");
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/profile/editPhoto.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part image = req.getPart("image");
        String imageName = profileService.setImageName(image);

        try {
            User authUser = (User) req.getSession().getAttribute("authUser");
            String realPath = getServletContext().getRealPath("");
            String role = authUser.isAdmin() ? "/a" : "/u";
            if (profileService.editPhoto(authUser, image, imageName, realPath)) {
                logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                        role + "/profile?id=" + authUser.getId()));
                resp.sendRedirect(req.getContextPath() + role + "/profile?id=" + authUser.getId());
                return;
            }
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            req.getSession().setAttribute("messageError", bundle.getString("message.err_photo_update"));
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    role + "/edit-photo"));
            resp.sendRedirect(req.getContextPath() + role + "/edit-photo");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
