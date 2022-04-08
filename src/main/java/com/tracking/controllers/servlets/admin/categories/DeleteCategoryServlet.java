package com.tracking.controllers.servlets.admin.categories;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.categories.CategoryService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet, that responsible for deleting category (admin)
 */
@WebServlet("/a/delete-cat")
public class DeleteCategoryServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeleteCategoryServlet.class);

    CategoryService categoryService = null;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int categoryId = Integer.parseInt(req.getParameter("id"));

        try {
            categoryService.delete(req, categoryId);
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    "/a/categories"));
            resp.sendRedirect(req.getContextPath() + "/a/categories");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
