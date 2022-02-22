package com.tracking.controllers.servlets.admin.activities;

import com.tracking.controllers.constants.FilePaths;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.models.Activity;
import com.tracking.services.activities.ActivityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/a/delete-act")
public class DeleteActivityServlet extends HttpServlet {

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
        try {
            Activity activity = activityService.get(activityId);
            activityService.delete(req.getSession(), activityId);
            activityService.deleteImage(activity.getImage(), getServletContext().getRealPath(""));
            resp.sendRedirect(req.getContextPath() + "/a/activities");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
