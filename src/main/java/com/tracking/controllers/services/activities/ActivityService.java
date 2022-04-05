package com.tracking.controllers.services.activities;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.CategoryDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.lang.Language;
import com.tracking.models.Activity;
import com.tracking.models.Category;
import com.tracking.models.User;
import com.tracking.models.UserActivity;
import com.tracking.controllers.services.Service;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Service, that contains all actions with activity
 */
public class ActivityService extends Service {

    private static final Logger logger = Logger.getLogger(ActivityService.class);

    /**
     * Creating activity
     * @param req for getting session to get/set attributes, getting parameters
     * @param activity activity for creating
     * @return true if activity is created
     * @throws ServiceException if something went wrong while executing
     */
    public boolean add(HttpServletRequest req, Activity activity) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        boolean result = false;
        try {
            result = activityDAO.create(req, activity);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: add was failed", e);
        }
        return result;
    }

    /**
     * Updating activity
     * @param req for getting session to get/set attributes, getting parameters
     * @param activity activity for updating
     * @return true if activity is updated
     * @throws ServiceException if something went wrong while executing
     */
    public boolean update(HttpServletRequest req, Activity activity) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        boolean result = false;
        try {
            result = activityDAO.update(req, activity);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: update was failed", e);
        }
        return result;
    }

    /**
     * Deleting activity
     * @param activityId id of activity for deleting
     * @throws ServiceException if something went wrong while executing
     */
    public void delete(int activityId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            activityDAO.delete(activityId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: delete was failed", e);
        }
    }

    /**
     * Getting activity
     * @param activityId id of activity for getting
     * @return received activity
     * @throws ServiceException if something went wrong while executing
     */
    public Activity get(int activityId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        Activity activity = null;
        try {
            activity = activityDAO.getById(activityId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: get was failed", e);
        }
        return activity;
    }

    /**
     * Getting all selected ids of categories after creating/updating activity
     * @param req for getting session to get/set attributes, getting parameters
     * @return list of selected category ids
     */
    public List<Integer> getAllCategoryIds(HttpServletRequest req) {
        String[] ids = req.getParameterValues("activityCategory");
        if (ids != null && ids.length > 0)
            return Arrays.stream(req.getParameterValues("activityCategory"))
                    .map(Integer::parseInt)
                    .toList();
        return null;
    }

    /**
     * Getting categories of activity
     * @param categoryIds category ids of activity
     * @param locale for setting order by name in given language
     * @return list of categories
     * @throws ServiceException if something went wrong while executing
     */
    public List<Category> getActivityCategories(List<Integer> categoryIds, Locale locale) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        CategoryDAO categoryDAO = factory.getCategoryDao();
        List<Category> categories = null;
        if (categoryIds == null || categoryIds.isEmpty()) {
            categories = new ArrayList<>();
            categories.add(new Category(0, "Other", "Інше"));
        } else {
            try {
                categories = categoryDAO.getAllById(categoryIds, locale); // localize
            } catch (DBException e) {
                e.printStackTrace();
                logger.error(e);
                throw new ServiceException("ActivityService: getActivityCategories was failed", e);
            }
        }
        return categories;
    }

    /**
     * Getting users, that not in selected activity
     * @param activityId id of selected activity
     * @return list of available users
     * @throws ServiceException if something went wrong while executing
     */
    public List<User> getAvailableUsers(int activityId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        List<User> userList = null;
        try {
            userList = userDAO.getAllWhereNotInActivity(activityId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: getAvailableUsers was failed", e);
        }
        return userList;
    }

    /**
     * Getting creator of selected activity
     * @param activityId id of selected activity
     * @return creator of activity
     * @throws ServiceException if something went wrong while executing
     */
    public User getCreator(int activityId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        User creator = null;
        try {
            creator = activityDAO.getCreator(activityId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: getCreator was failed", e);
        }
        return creator;
    }

    /**
     * Getting user's information in selected activity
     * @param user authorized user
     * @param activityId id of selected activity
     * @return information of activity and user's information there
     * @throws ServiceException if something went wrong while executing
     */
    public UserActivity getUserInfo(User user, int activityId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        UserActivity creatorInfo = null;
        try {
            creatorInfo = userDAO.getWhereInActivity(user, activityId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: getUserInfo was failed", e);
        }
        return creatorInfo;
    }

    /**
     * Getting all essential for Activity page
     * @param req for getting session to get/set attributes, getting parameters
     * @param resp for sending redirect
     * @return true if process of activity was successful, false if authorized user not admin or not in activity,
     * if request parameter "page" equal or less than 0 and greater than assigned page count
     * @throws IOException if something went wrong while redirecting
     * @throws ServiceException if something went wrong while executing
     */
    public boolean processActivity(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
            UserDAO userDAO = factory.getUserDao();
            ActivityDAO activityDAO = factory.getActivityDao();
            int activityId = Integer.parseInt(req.getParameter("id"));

            HttpSession session = req.getSession();
            User authUser = (User) session.getAttribute("authUser");
            UserActivity userActivity = null;
            if (!authUser.isAdmin())
                userActivity = getUserInfo(authUser, activityId);
            if (authUser.isAdmin() || userActivity != null) {
                Activity activity = get(activityId);
                List<Category> categories = getActivityCategories(activity.getCategories(), Service.getLocale(req)); // localize
                if (authUser.isAdmin()) {
                    List<User> usersNotInActivity = getAvailableUsers(activityId);
                    User creator = getCreator(activityId);
                    List<UserActivity> userList;

                    int start = 1;
                    int page = 1;
                    if (req.getParameter("page") != null) {
                        page = Integer.parseInt(req.getParameter("page"));
                        if (page <= 0) {
                            redirectWithoutPage(req, resp, "activity", authUser);
                            return false;
                        }
                        start = start + TOTAL_USERS_ACTIVITY * (page - 1);
                    }

                    int userActivityCount;
                    String sort = req.getParameter("sort");
                    String order = req.getParameter("order");
                    if (order == null || order.isEmpty())
                        order = "asc";
                    if (req.getParameter("lastName") != null || req.getParameter("firstName") != null) {
                        String lastName = req.getParameter("lastName");
                        String firstName = req.getParameter("firstName");
                        req.setAttribute("lastName", lastName);
                        req.setAttribute("firstName", firstName);

                        userActivityCount = activityDAO.getCountInActivityWhereName(activityId, lastName, firstName);
                        userList = userDAO.getAllInActivityWhereName(activityId, lastName, firstName, sort, order, start, TOTAL_USERS_ACTIVITY);
                    } else {
                        userActivityCount = activityDAO.getCountInActivity(activityId);
                        userList = userDAO.getAllWhereInActivity(activityId, sort, order, start, TOTAL_USERS_ACTIVITY);
                    }

                    int pageCount = getPageCount(userActivityCount, TOTAL_USERS_ACTIVITY);
                    if (pageCount > 0 && page > pageCount) {
                        redirectWithoutPage(req, resp, "activity", authUser);
                        return false;
                    }

                    int previousPage = 0;
                    if (page > 1)
                        previousPage = page - 1;
                    int nextPage = 0;
                    if (userActivityCount > TOTAL_USERS_ACTIVITY && page < pageCount)
                        nextPage = page + 1;

                    req.setAttribute("pageCount", pageCount);
                    req.setAttribute("previousPage", previousPage);
                    req.setAttribute("nextPage", nextPage);
                    req.setAttribute("users", userList);
                    req.setAttribute("usersNotInActivity", usersNotInActivity);
                    req.setAttribute("creator", creator);
                }
                req.setAttribute("activity", activity);
                req.setAttribute("categories", categories);
                req.setAttribute("userActivity", userActivity);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return false;
            }
        } catch (DBException | ServiceException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: processActivity was failed", e);
        }
        return true;
    }

    /**
     * Adding user to activity
     * @param activityId id of activity
     * @param userId id of selected user
     * @throws ServiceException if something went wrong while executing
     */
    public void addUser(int activityId, int userId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            activityDAO.addUser(activityId, userId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: addUser was failed", e);
        }
    }

    /**
     * Removing user from activity
     * @param activityId id of activity
     * @param userId id of selected user
     * @throws ServiceException if something went wrong while executing
     */
    public void removeUser(int activityId, int userId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            activityDAO.deleteUser(activityId, userId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: removeUser was failed", e);
        }
    }

    /**
     * Starting the activity
     * @param activityId id of activity
     * @param userId id of user
     * @throws ServiceException if something went wrong while executing
     */
    public void startTime(int activityId, int userId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            userDAO.startTime(activityId, userId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: startTime was failed", e);
        }
    }

    /**
     * Stopping the activity
     * @param activityId id of activity
     * @param userId id of user
     * @throws ServiceException if something went wrong while executing
     */
    public void stopTime(int activityId, int userId) throws ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        try {
            userDAO.stopTime(activityId, userId);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ActivityService: stopTime was failed", e);
        }
    }
}
