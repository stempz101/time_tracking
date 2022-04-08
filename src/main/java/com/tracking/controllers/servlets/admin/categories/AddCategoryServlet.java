package com.tracking.controllers.servlets.admin.categories;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.categories.CategoryService;
import com.tracking.controllers.servlets.admin.activities.RemoveUserServlet;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet, that responsible for showing Add Category page and creating action (admin)
 */
@WebServlet("/a/add-cat")
public class AddCategoryServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AddCategoryServlet.class);

    CategoryService categoryService = null;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("lang") != null)
            req.getSession().setAttribute("lang", req.getParameter("lang"));
        logger.info("Opening Add Category page (admin)");
        req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/categories/addCategory.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameEN = req.getParameter("categoryEN").strip();
        String nameUA = req.getParameter("categoryUA").strip();

        HttpSession session = req.getSession();
        saveFields(session, nameEN, nameUA);

        try {
            categoryService.add(req, nameEN, nameUA);
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    "/a/add-cat"));
            resp.sendRedirect(req.getContextPath() + "/a/add-cat");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    private void saveFields(HttpSession session, String nameEN, String nameUA) {
        session.setAttribute("categoryEN", nameEN);
        session.setAttribute("categoryUA", nameUA);
    }
}
