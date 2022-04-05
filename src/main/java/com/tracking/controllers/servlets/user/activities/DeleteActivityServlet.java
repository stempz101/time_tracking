package com.tracking.controllers.servlets.user.activities;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.models.Activity;
import com.tracking.controllers.services.activities.ActivityService;
import com.tracking.controllers.services.requests.RequestService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Servlet, that responsible for creating request for remove (user)
 */
@WebServlet("/u/delete-act")
public class DeleteActivityServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DeleteActivityServlet.class);

    ActivityService activityService = null;
    RequestService requestService = null;

    @Override
    public void init() throws ServletException {
        activityService = new ActivityService();
        requestService = new RequestService();
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
            requestService.addForDelete(req, activity);
            ResourceBundle bundle = ResourceBundle.getBundle("content", Service.getLocale(req));
            req.getSession().setAttribute("successMessage", bundle.getString("message.req_sent_remove"));
            logger.info("Redirecting to " + Service.getFullURL(req, "/u/activities"));
            resp.sendRedirect(req.getContextPath() + "/u/activities");
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
