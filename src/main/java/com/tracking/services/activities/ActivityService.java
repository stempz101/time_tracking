package com.tracking.services.activities;

import com.tracking.controllers.constants.FilePaths;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.lang.Language;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.models.User;
import com.tracking.models.UserActivity;
import com.tracking.services.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ActivityService extends Service {

    public boolean add(HttpSession session, Activity activity) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        boolean result = false;
        try {
            result = activityDAO.create(session, activity);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return result;
    }

    public boolean update(HttpSession session, Activity activity) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        boolean result = false;
        try {
            result = activityDAO.update(session, activity);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return result;
    }

    public void delete(HttpSession session, int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            Activity activity = activityDAO.getById(activityId);
            activityDAO.delete(session, activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public Activity get(int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        Activity activity = null;
        try {
            activity = activityDAO.getById(activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return activity;
    }

    public List<Integer> getAllCategoryIds(HttpServletRequest req) {
        String[] ids = req.getParameterValues("activityCategory");
        if (ids != null && ids.length > 0)
            return Arrays.stream(req.getParameterValues("activityCategory"))
                    .map(Integer::parseInt)
                    .toList();
        return null;
    }

    public List<Category> getActivityCategories(List<Integer> categoryIds, Language language) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        List<Category> categories = null;
        try {
            categories = categoryDAO.getAllById(categoryIds, language); // localize
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return categories;
    }

    public List<UserActivity> getActivityUsers(int activityId, int start, int total) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<UserActivity> userList = null;
        try {
            userList = userDAO.getAllWhereInActivity(activityId, start, total);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    public List<User> getAvailableUsers(int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<User> userList = null;
        try {
            userList = userDAO.getAllWhereNotInActivity(activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return userList;
    }

    public User getCreator(int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        User creator = null;
        try {
            creator = activityDAO.getCreator(activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return creator;
    }

    public UserActivity getCreatorInfo(User creator, int activityId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        UserActivity creatorInfo = null;
        try {
            creatorInfo = userDAO.getWhereInActivity(creator, activityId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return creatorInfo;
    }

    public boolean processActivity(HttpServletRequest req) throws SQLException {
        try {
            int activityId = Integer.parseInt(req.getParameter("id"));
            Activity activity = get(activityId);

            int userActivityCount = activity.getPeopleCount();
            int start = 1;
            int page = 1;
            int pageCount = getPageCount(userActivityCount, TOTAL_USERS_ACTIVITY);
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0 || page > pageCount)
                    return false;
                start = start + TOTAL_USERS_ACTIVITY * (page - 1);
            }
            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (userActivityCount > TOTAL_USERS_ACTIVITY && page < pageCount)
                nextPage = page + 1;

            HttpSession session = req.getSession();
            List<Category> categories = getActivityCategories(activity.getCategories(), Language.EN); // localize
            List<UserActivity> users = getActivityUsers(activityId, start, TOTAL_USERS_ACTIVITY);
            UserActivity userActivity = getCreatorInfo((User) session.getAttribute("authUser"), activityId);
            List<User> usersNotInActivity = getAvailableUsers(activityId);
            User creator = getCreator(activityId);

            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("activity", activity);
            req.setAttribute("categories", categories);
            req.setAttribute("users", users);
            req.setAttribute("userActivity", userActivity);
            req.setAttribute("usersNotInActivity", usersNotInActivity);
            req.setAttribute("creator", creator);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
        return true;
    }

    public void addUser(int activityId, int userId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            activityDAO.addUser(activityId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void removeUser(int activityId, int userId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            activityDAO.deleteUser(activityId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void startTime(int activityId, int userId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            userDAO.startTime(activityId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void stopTime(int activityId, int userId) throws SQLException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            userDAO.stopTime(activityId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }
}
