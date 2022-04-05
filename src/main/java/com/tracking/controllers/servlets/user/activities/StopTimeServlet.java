package com.tracking.controllers.servlets.user.activities;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.models.User;
import com.tracking.controllers.services.activities.ActivityService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet, that responsible for stopping activity (user)
 */
@WebServlet("/u/activity-stop")
public class StopTimeServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(StopTimeServlet.class);

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
            activityService.stopTime(activityId, authUser.getId());
            logger.info("Redirecting to " + Service.getFullURL(req, "/u/activity?id=" + activityId));
            resp.sendRedirect(req.getContextPath() + "/u/activity?id=" + activityId);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
