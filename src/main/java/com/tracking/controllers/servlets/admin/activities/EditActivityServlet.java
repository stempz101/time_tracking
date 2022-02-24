package com.tracking.controllers.servlets.admin.activities;

import com.tracking.controllers.constants.FilePaths;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.lang.Language;
import com.tracking.services.activities.ActivityService;
import com.tracking.services.categories.CategoriesService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebServlet("/a/edit-act")
@MultipartConfig
public class EditActivityServlet extends HttpServlet {

    ActivityService activityService = null;
    CategoriesService categoriesService = null;

    @Override
    public void init() throws ServletException {
        activityService = new ActivityService();
        categoriesService = new CategoriesService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        HttpSession session = req.getSession();

        try {
            Activity activity = activityService.get(id);
            List<Category> categoryList = categoriesService.getAllCategories(Language.EN); // localize
            session.setAttribute("activity", activity);
            session.setAttribute("categoryList", categoryList);

            ServletContext context = getServletContext();
            RequestDispatcher requestDispatcher = context.getRequestDispatcher("/jsp/admin/activities/editActivity.jsp");
            requestDispatcher.forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("activityName");
        List<Integer> categoryIds = activityService.getAllCategoryIds(req);
        String description = req.getParameter("activityDescription");
        Part image = req.getPart("activityImage");
        String imageName = activityService.setImageName(image);

        HttpSession session = req.getSession();
        Activity activity = (Activity) session.getAttribute("activity");
        String oldImage = activity.getImage();
        saveFields(name, categoryIds, description, imageName, activity);

        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            boolean result = activityService.update(session, activity);
            activityService.updateActivityImage(image, imageName, oldImage, getServletContext().getRealPath(""));
            if (result) {
                resp.sendRedirect(req.getContextPath() + "/a/activities");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/a/edit-act?id=" + activity.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveFields(String name, List<Integer> categoryIds, String description, String image, Activity activity) {
        activity.setName(name);
        activity.setCategories(categoryIds);
        activity.setDescription(description);
        if (image != null && !image.isEmpty())
            activity.setImage(image);
    }
}
