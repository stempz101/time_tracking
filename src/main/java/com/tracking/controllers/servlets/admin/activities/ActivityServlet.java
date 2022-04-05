package com.tracking.controllers.servlets.admin.activities;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.activities.ActivityService;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet, that responsible for showing Activity page (admin)
 */
@WebServlet("/a/activity")
public class ActivityServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ActivityServlet.class);

    ActivityService activityService = null;

    @Override
    public void init() throws ServletException {
        activityService = new ActivityService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Service.setLang(req);
            if (!activityService.processActivity(req, resp))
                return;
            activityService.setQueryStringForPagination(req);
            logger.info("Opening Activity (id=" + req.getParameter("id") + ") page (admin)");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/activities/activity/activity.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
