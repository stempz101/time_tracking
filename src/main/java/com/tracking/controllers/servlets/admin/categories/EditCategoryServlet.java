package com.tracking.controllers.servlets.admin.categories;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.models.Category;
import com.tracking.controllers.services.categories.CategoryService;
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
 * Servlet, that responsible for showing Edit Category page and updating action (admin)
 */
@WebServlet("/a/edit-cat")
public class EditCategoryServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EditCategoryServlet.class);

    CategoryService categoryService = null;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Service.setLang(req);
        int categoryId = Integer.parseInt(req.getParameter("id"));
        Category category = null;
        try {
            category = categoryService.get(categoryId);

            HttpSession session = req.getSession();
            session.setAttribute("category", category);

            logger.info("Opening Edit Category page (admin)");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/categories/editCategory.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameEN = req.getParameter("categoryEN");
        String nameUA = req.getParameter("categoryUA");

        HttpSession session = req.getSession();
        saveFields(session, nameEN, nameUA);

        try {
            Category category = (Category) session.getAttribute("category");
            boolean result = categoryService.update(req, nameEN, nameUA);
            if (result) {
                logger.info("Redirecting to " + Service.getFullURL(req, "/a/categories"));
                resp.sendRedirect(req.getContextPath() + "/a/categories");
                return;
            }
            logger.info("Redirecting to " + Service.getFullURL(req, "/a/edit-cat?id=" + category.getId()));
            resp.sendRedirect(req.getContextPath() + "/a/edit-cat?id=" + category.getId());
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
