package com.tracking.controllers.services.profile;

import com.tracking.controllers.exceptions.DBException;
import com.tracking.controllers.exceptions.ServiceException;
import com.tracking.controllers.services.Service;
import com.tracking.controllers.services.categories.CategoryService;
import com.tracking.dao.ActivityDAO;
import com.tracking.dao.DAOFactory;
import com.tracking.dao.UserDAO;
import com.tracking.models.Activity;
import com.tracking.models.User;
import com.tracking.models.UserActivity;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Service, that contains all actions with profile
 */
public class ProfileService extends Service {

    private static final Logger logger = Logger.getLogger(ProfileService.class);

    /**
     * Getting all essential for Profile page
     * @param req for getting session to get/set attributes, getting parameters
     * @param resp for sending redirect
     * @return true if process of profile was successful, false if user under role "user" try to get information about
     * others, if request parameter "page" equal or less than 0 and
     * @throws IOException if something went wrong while redirecting
     * @throws ServiceException if something went wrong while executing
     */
    public boolean processProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
        UserDAO userDAO = factory.getUserDao();
        ActivityDAO activityDAO = factory.getActivityDao();
        try {
            User authUser = (User) req.getSession().getAttribute("authUser");
            int userId = Integer.parseInt(req.getParameter("id"));
            User user = userDAO.getById(userId);

            int start = 1;
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
                if (page <= 0) {
                    redirectWithoutPage(req, resp, "profile", authUser);
                    return false;
                }
                start = start + TOTAL_ACTIVITIES * (page - 1);
            }

            List<Activity> activityAdminList = new ArrayList<>();
            List<UserActivity> activityUserList = new ArrayList<>();
            int activityCount;
            if (authUser.isAdmin()) {
                if (user.isAdmin()) {
                    activityCount = activityDAO.getCreatedCount(user.getId());
                    activityAdminList = activityDAO.getAllCreated(user.getId(), start, TOTAL_ACTIVITIES);
                } else {
                    activityCount = activityDAO.getCountForProfile(user.getId());
                    activityUserList = activityDAO.getAllForProfile(user.getId(), start, TOTAL_ACTIVITIES);
                }
            } else {
                if (user.getId() == authUser.getId()) {
                    activityCount = activityDAO.getCountForProfile(user.getId());
                    activityUserList = activityDAO.getAllForProfile(user.getId(), start, TOTAL_ACTIVITIES);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return false;
                }
            }

            int pageCount = getPageCount(activityCount, TOTAL_ACTIVITIES);
            if (pageCount > 0 && page > pageCount) {
                redirectWithoutPage(req, resp, "profile", authUser);
                return false;
            }

            int previousPage = 0;
            if (page > 1)
                previousPage = page - 1;
            int nextPage = 0;
            if (activityCount > TOTAL_ACTIVITIES && page < pageCount)
                nextPage = page + 1;
            req.setAttribute("pageCount", pageCount);
            req.setAttribute("previousPage", previousPage);
            req.setAttribute("nextPage", nextPage);
            req.setAttribute("user", user);
            req.setAttribute("activityCount", activityCount);
            if (user.isAdmin())
                req.setAttribute("adminActivities", activityAdminList);
            else
                req.setAttribute("userActivities", activityUserList);

            return true;
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ProfileService: processProfile was failed", e);
        }
    }

    /**
     * Updating user information (last name, first name, email)
     * @param req for getting session to get/set attributes, getting parameters
     * @param userId id of user
     * @param lastName updated last name of user
     * @param firstName updated first name of user
     * @param email updated email of user
     * @return true if user is updated
     * @throws ServiceException if something went wrong while executing
     */
    public boolean editProfile(HttpServletRequest req, int userId, String lastName, String firstName,
                            String email) throws ServiceException {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            if (!UserDAO.validateName(lastName)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.last_name_error"));
                return false;
            }
            if (!UserDAO.validateName(firstName)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.first_name_error"));
                return false;
            }
            if (!UserDAO.validateEmail(email)) {
                req.getSession().setAttribute("messageError", bundle.getString("message.email_error"));
                return false;
            }

            DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
            UserDAO userDAO = factory.getUserDao();
            return userDAO.updateProfile(userId, lastName, firstName, email);
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ProfileService: editProfile was failed", e);
        }
    }

    /**
     * Updating user photo
     * @param authUser authorized user
     * @param image new image
     * @param imageName created name for new image
     * @param realPath upload path of user photo
     * @return true if user photo is updated and saved
     * @throws ServiceException if something went wrong while executing
     */
    public boolean editPhoto(User authUser, Part image, String imageName, String realPath) throws ServiceException {
        try {
            DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
            UserDAO userDAO = factory.getUserDao();

            if (userDAO.updatePhoto(authUser.getId(), imageName)) {
                updateUserImage(image, imageName, authUser.getImage(), realPath);
                return true;
            }
            return false;
        } catch (DBException | ServiceException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ProfileService: editPhoto was failed", e);
        }
    }

    /**
     * Updating user password
     * @param req for getting session to get/set attributes, getting parameters
     * @param userId id of user
     * @param currentPassword current password of user
     * @param newPassword new created password
     * @param confirmPassword confirmation of new password
     * @return true if user password is updated
     * @throws ServiceException for getting session to get/set attributes, getting parameters
     */
    public boolean editPassword(HttpServletRequest req, int userId, String currentPassword,
                                String newPassword, String confirmPassword) throws ServiceException {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("content",
                    Service.getLocale((String) req.getSession().getAttribute("lang")));
            DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.FactoryType.MYSQL);
            UserDAO userDAO = factory.getUserDao();

            User authUser = userDAO.getById(userId);
            if (userDAO.checkPassword(authUser, currentPassword)) {
                if (!UserDAO.validatePassword(newPassword)) {
                    req.getSession().setAttribute("messageError", bundle.getString("message.new_password_error"));
                    return false;
                }
                if (!UserDAO.confirmPassword(newPassword, confirmPassword)) {
                    req.getSession().setAttribute("messageError", bundle.getString("message.confirm_password_error"));
                    return false;
                }

                return userDAO.updatePassword(userId, currentPassword, newPassword, confirmPassword);
            }
            req.getSession().setAttribute("messageError", bundle.getString("message.password_incorrect"));
            return false;
        } catch (DBException e) {
            e.printStackTrace();
            logger.error(e);
            throw new ServiceException("ProfileService: editPassword was failed", e);
        }
    }
}
