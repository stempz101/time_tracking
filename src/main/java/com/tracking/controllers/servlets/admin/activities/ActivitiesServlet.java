package com.tracking.controllers.servlets.admin.activities;

import com.tracking.services.activities.ActivitiesService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/a/activities")
public class ActivitiesServlet extends HttpServlet {

    private ActivitiesService activitiesService = null;

    @Override
    public void init() throws ServletException {
        activitiesService = new ActivitiesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            activitiesService.processActivities(req);
            if (req.getQueryString() != null)
                activitiesService.setQueryStringForPagination(req);

            ServletContext context = getServletContext();
            RequestDispatcher requestDispatcher = context.getRequestDispatcher("/jsp/admin/activities/activities.jsp");
            requestDispatcher.forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
