package com.tracking.controllers.servlets.general.activity;

import com.tracking.dao.ActivityDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.models.User;
import com.tracking.services.activities.ActivityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/u/activity-add-user", "/a/activity-add-user"})
public class AddUserServlet extends HttpServlet {

    ActivityService activityService = null;

    @Override
    public void init() throws ServletException {
        activityService = new ActivityService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int activityId = Integer.parseInt(req.getParameter("activity"));
        int userId = Integer.parseInt(req.getParameter("user"));

        try {
            activityService.addUser(activityId, userId);
            req.setAttribute("successMessage", "User (id = " + userId + ") was added to activity successfully");

            User authUser = (User) req.getSession().getAttribute("authUser");
            if (authUser.isAdmin())
                resp.sendRedirect(req.getContextPath() + "/a/activity?id=" + activityId);
            else
                resp.sendRedirect(req.getContextPath() + "/u/activity?id=" + activityId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
