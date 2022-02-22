package com.tracking.controllers.servlets.admin.users;

import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.User;

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

@WebServlet("/a/users")
public class UsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getProcess(req, resp);
    }

    private void getProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            int userCount = userDAO.getCount();
            int start = 1;
            int total = 10;
            int page = 1;
            int pageCount = userCount % total == 0 ? userCount / total
                    : userCount / total + 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0 || page > pageCount) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                start = start + total * (page - 1);
            }
            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (userCount > total && page < pageCount)
                nextPage = page + 1;

            List<User> userList = userDAO.getAll(start, total);

            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("userList", userList);

            ServletContext context = getServletContext();
            RequestDispatcher requestDispatcher = context.getRequestDispatcher("/jsp/admin/users/users.jsp");
            requestDispatcher.forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
