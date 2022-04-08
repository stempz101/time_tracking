package com.tracking.controllers.servlets.admin.activities;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.lang.Language;
import com.tracking.controllers.services.activities.ActivityService;
import com.tracking.controllers.services.categories.CategoriesService;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet, that responsible for showing Edit Activity page and updating activity (admin)
 */
@WebServlet("/a/edit-act")
@MultipartConfig
public class EditActivityServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(EditActivityServlet.class);

    ActivityService activityService = null;
    CategoriesService categoriesService = null;

    @Override
    public void init() throws ServletException {
        activityService = new ActivityService();
        categoriesService = new CategoriesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("lang") != null)
            req.getSession().setAttribute("lang", req.getParameter("lang"));
        int id = Integer.parseInt(req.getParameter("id"));
        HttpSession session = req.getSession();

        try {
            Activity activity = activityService.get(id);
            List<Category> categoryList = categoriesService
                    .getAllCategories(Service.getLocale((String) req.getSession().getAttribute("lang"))); // localize
            session.setAttribute("activity", activity);
            session.setAttribute("categoryList", categoryList);
            logger.info("Opening Edit Activity page (admin)");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/admin/activities/editActivity.jsp").forward(req, resp);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("activityName").strip();
        List<Integer> categoryIds = activityService.getAllCategoryIds(req);
        String description = req.getParameter("activityDescription").strip();
        Part image = req.getPart("activityImage");
        String imageName = activityService.setImageName(image);

        Activity activity = (Activity) req.getSession().getAttribute("activity");
        String oldImage = activity.getImage();
        saveFields(name, categoryIds, description, imageName, activity);

        try {
            if (activityService.update(req, activity)) {
                activityService.updateActivityImage(image, imageName, oldImage, getServletContext().getRealPath(""));
                logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                        "/a/activities"));
                resp.sendRedirect(req.getContextPath() + "/a/activities");
                return;
            }
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    "/a/edit-act?id=" + activity.getId()));
            resp.sendRedirect(req.getContextPath() + "/a/edit-act?id=" + activity.getId());
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    private void saveFields(String name, List<Integer> categoryIds, String description, String image, Activity activity) {
        activity.setName(name);
        activity.setCategories(categoryIds);
        activity.setDescription(description);
        activity.setImage(image);
    }
}
