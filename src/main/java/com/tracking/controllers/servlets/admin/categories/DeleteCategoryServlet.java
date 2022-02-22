package com.tracking.controllers.servlets.admin.categories;

import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.services.categories.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/a/delete-cat")
public class DeleteCategoryServlet extends HttpServlet {

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
            categoryService.delete(req.getSession(), categoryId);
            resp.sendRedirect(req.getContextPath() + "/a/categories");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
