package com.tracking.controllers.servlets.admin.activities;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.activities.ActivitiesService;
import com.tracking.controllers.servlets.RegisterServlet;
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
 * Servlet, that responsible for showing Activities page (admin)
 */
@WebServlet("/a/activities")
public class ActivitiesServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ActivitiesServlet.class);

    private ActivitiesService activitiesService = null;

    @Override
    public void init() throws ServletException {
        activitiesService = new ActivitiesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getParameter("lang") != null)
                req.getSession().setAttribute("lang", req.getParameter("lang"));
            if (!activitiesService.processActivities(req, resp))
                return;
            activitiesService.setQueryStringForPagination(req);
            logger.info("Opening Activities page (admin)");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/activities/activities.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
