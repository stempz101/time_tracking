package com.tracking.controllers.servlets.admin.categories;

import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.models.Category;
import com.tracking.services.categories.CategoryService;

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

@WebServlet("/a/edit-cat")
public class EditCategoryServlet extends HttpServlet {

    CategoryService categoryService = null;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int categoryId = Integer.parseInt(req.getParameter("id"));
        Category category = null;
        try {
            category = categoryService.get(categoryId);

            HttpSession session = req.getSession();
            session.setAttribute("category", category);

            ServletContext context = getServletContext();
            RequestDispatcher requestDispatcher = context.getRequestDispatcher("/jsp/admin/categories/editCategory.jsp");
            requestDispatcher.forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
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
            boolean result = categoryService.update(session, nameEN, nameUA);
            if (result) {
                resp.sendRedirect(req.getContextPath() + "/a/categories");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/a/edit-cat?id=" + category.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveFields(HttpSession session, String nameEN, String nameUA) {
        session.setAttribute("categoryEN", nameEN);
        session.setAttribute("categoryUA", nameUA);
    }
}
