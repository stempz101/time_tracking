package com.tracking.controllers.servlets.user.activities;

import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.lang.Language;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.models.User;
import com.tracking.controllers.services.activities.ActivityService;
import com.tracking.controllers.services.categories.CategoriesService;
import com.tracking.controllers.services.requests.RequestService;
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
import java.util.ResourceBundle;

/**
 * Servlet, that responsible for showing Add Activity page and creating request for add (user)
 */
@WebServlet("/u/add-act")
@MultipartConfig
public class AddActivityServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AddActivityServlet.class);

    ActivityService activityService = null;
    CategoriesService categoriesService = null;
    RequestService requestService = null;

    @Override
    public void init() throws ServletException {
        activityService = new ActivityService();
        categoriesService = new CategoriesService();
        requestService = new RequestService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getParameter("lang") != null)
                req.getSession().setAttribute("lang", req.getParameter("lang"));
            List<Category> categoryList = categoriesService
                    .getAllCategories(Service.getLocale((String) req.getSession().getAttribute("lang")));  // localize
            req.setAttribute("categoryList", categoryList);
            logger.info("Opening Add Activity page (user)");
            req.getServletContext().getRequestDispatcher("/WEB-INF/jsp/user/activities/addActivity.jsp").forward(req, resp);
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

        HttpSession session = req.getSession();
        User authUser = (User) session.getAttribute("authUser");
        Activity activity = (Activity) session.getAttribute("activity");
        if (activity == null)
            activity = new Activity(name, categoryIds, description, imageName, authUser.getId(), Activity.Status.ADD_WAITING);
        else
            saveFields(name, categoryIds, description, imageName, activity);
        session.setAttribute("activity", activity);

        try {
            if (requestService.addForAdd(req, activity)) {
                activityService.saveActivityImage(image, imageName, getServletContext().getRealPath(""));
                ResourceBundle bundle = ResourceBundle.getBundle("content",
                        Service.getLocale((String) req.getSession().getAttribute("lang")));
                req.getSession().setAttribute("successMessage", bundle.getString("message.req_sent_add"));
            }
            logger.info("Redirecting to " + Service.getFullURL(req.getRequestURL().toString(), req.getRequestURI(),
                    "/u/add-act"));
            resp.sendRedirect(req.getContextPath() + "/u/add-act");
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
