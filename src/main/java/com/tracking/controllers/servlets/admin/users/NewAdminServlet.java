package com.tracking.controllers.servlets.admin.users;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.RegisterService;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.users.UsersService;
import com.tracking.controllers.servlets.admin.requests.RequestsServlet;
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
 * Servlet, that responsible for showing New Admin page and creating action (admin)
 */
@WebServlet("/a/new-adm")
@MultipartConfig
public class NewAdminServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(NewAdminServlet.class);

    RegisterService registerService = null;

    @Override
    public void init() throws ServletException {
        registerService = new RegisterService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("lang") != null)
            req.getSession().setAttribute("lang", req.getParameter("lang"));
        logger.info("Opening New Admin page (admin)");
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/users/newAdmin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lastName = req.getParameter("last_name").strip();
        String firstName = req.getParameter("first_name").strip();
        String email = req.getParameter("email").strip();
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        Part image = req.getPart("image");
        String imageName = registerService.setImageName(image);

        try {
            User user = registerService.registerAdmin(req, lastName, firstName, email, password, confirmPassword, imageName);
            if (user == null) {
                logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                        "/a/new-adm"));
                resp.sendRedirect(req.getContextPath() + "/a/new-adm");
                return;
            }

            registerService.saveUserImage(image, user.getImage(), getServletContext().getRealPath(""));
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            req.getSession().setAttribute("successMessage", bundle.getString("message.admin_created"));
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    "/a/new-adm"));
            resp.sendRedirect(req.getContextPath() + "/a/new-adm");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
