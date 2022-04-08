package com.tracking.controllers.servlets.admin.categories;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.categories.CategoriesService;
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
 * Servlet, that responsible for showing Categories page (admin)
 */
@WebServlet("/a/categories")
public class CategoriesServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(CategoriesServlet.class);

    CategoriesService categoriesService = null;

    @Override
    public void init() throws ServletException {
        categoriesService = new CategoriesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getParameter("lang") != null)
                req.getSession().setAttribute("lang", req.getParameter("lang"));
            if (!categoriesService.processCategories(req, resp))
                return;
            categoriesService.setQueryStringForPagination(req);
            logger.info("Opening Categories page (admin)");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/categories/categories.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
