package com.tracking.controllers.servlets.admin.categories;

import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.models.Category;
import com.tracking.lang.Language;
import com.tracking.services.categories.CategoriesService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/a/categories")
public class CategoriesServlet extends HttpServlet {

    CategoriesService categoriesService = null;

    @Override
    public void init() throws ServletException {
        categoriesService = new CategoriesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!categoriesService.processCategories(req)) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            ServletContext context = getServletContext();
            RequestDispatcher requestDispatcher = context.getRequestDispatcher("/jsp/admin/categories/categories.jsp");
            requestDispatcher.forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
