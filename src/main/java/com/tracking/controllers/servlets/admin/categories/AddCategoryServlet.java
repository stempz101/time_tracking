package com.tracking.controllers.servlets.admin.categories;

import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
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

@WebServlet("/a/add-cat")
public class AddCategoryServlet extends HttpServlet {

    CategoryService categoryService = null;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher requestDispatcher = context.getRequestDispatcher("/jsp/admin/categories/addCategory.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameEN = req.getParameter("categoryEN");
        String nameUA = req.getParameter("categoryUA");

        HttpSession session = req.getSession();
        saveFields(session, nameEN, nameUA);

        try {
            categoryService.add(session, nameEN, nameUA);
            resp.sendRedirect(req.getContextPath() + "/a/add-cat");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveFields(HttpSession session, String nameEN, String nameUA) {
        session.setAttribute("categoryEN", nameEN);
        session.setAttribute("categoryUA", nameUA);
    }
}
