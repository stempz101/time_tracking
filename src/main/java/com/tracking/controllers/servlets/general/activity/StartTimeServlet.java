package com.tracking.controllers.servlets.general.activity;

import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.User;
import com.tracking.services.activities.ActivityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/u/activity-start", "/a/activity-start"})
public class StartTimeServlet extends HttpServlet {

    ActivityService activityService = null;

    @Override
    public void init() throws ServletException {
        activityService = new ActivityService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int activityId = Integer.parseInt(req.getParameter("id"));
        HttpSession session = req.getSession();

        try {
            User authUser = (User) session.getAttribute("authUser");
            activityService.startTime(activityId, authUser.getId());
            if (authUser.isAdmin())
                resp.sendRedirect(req.getContextPath() + "/a/activity?id=" + activityId);
            else
                resp.sendRedirect(req.getContextPath() + "/u/activity?id=" + activityId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
